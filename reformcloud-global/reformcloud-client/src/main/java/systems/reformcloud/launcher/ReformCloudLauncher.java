/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.launcher;

import io.netty.util.ResourceLeakDetector;
import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.commands.CommandManager;
import systems.reformcloud.libloader.LibraryLoader;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.network.packets.sync.out.PacketOutSyncExceptionThrown;
import systems.reformcloud.utility.ExitUtil;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.files.FileUtils;
import systems.reformcloud.utility.time.DateProvider;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 23.10.2018
 */

final class ReformCloudLauncher {
    /**
     * Main Method of ReformCloudClient
     *
     * @param args          The given args by the executor
     * @throws Throwable    Will be thrown if an error occurs
     */
    public static synchronized void main(String[] args) throws Throwable {
        final List<String> options = Arrays.asList(args);

        if (StringUtil.USER_NAME.equalsIgnoreCase("root")
                && StringUtil.OS_NAME.toLowerCase().contains("linux")
                && !options.contains("--ignore-root")) {
            System.out.println("You cannot run ReformCloud as root user");
            try {
                Thread.sleep(2000);
            } catch (final InterruptedException ignored) {
            }
            System.exit(ExitUtil.STARTED_AS_ROOT);
            return;
        }

        final long current = System.currentTimeMillis();

        Thread.setDefaultUncaughtExceptionHandler((thread, t) -> {
            if (t instanceof ThreadDeath)
                return;

            if (ReformCloudClient.getInstance() != null) {
                StringUtil.printError(ReformCloudClient.getInstance().getLoggerProvider(), "Exception caught", t);
                ReformCloudClient.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController", new PacketOutSyncExceptionThrown(t));
            } else
                t.printStackTrace(System.err);
        });

        System.out.println("Trying to startup ReformCloudClient...");
        System.out.println("Startup time: " + DateProvider.formatByDefaultFormat(current));

        new LibraryLoader().loadJarFileAndInjectLibraries();

        if (Files.exists(Paths.get("reformcloud/logs")))
            FileUtils.deleteFullDirectory(Paths.get("reformcloud/logs"));

        System.out.println();

        final LoggerProvider loggerProvider = new LoggerProvider();
        final CommandManager commandManager = new CommandManager();

        ReformCloudLibraryService.sendHeader(loggerProvider);

        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);

        loggerProvider.setDebug(options.contains("--debug"));
        new ReformCloudClient(loggerProvider, commandManager, options.contains("--ssl"), current);

        loggerProvider.info(ReformCloudClient.getInstance().getInternalCloudNetwork().getLoaded().getHelp_default());

        String line;
        try {
            while (true) {
                loggerProvider.getConsoleReader().setPrompt("");
                loggerProvider.getConsoleReader().resetPromptLine("", "", 0);

                while ((line = loggerProvider.getConsoleReader().readLine(StringUtil.REFORM_VERSION + "-" + StringUtil.REFORM_SPECIFICATION + "@ReformCloudClient > ")) != null && !line.trim().isEmpty() && ReformCloudClient.RUNNING) {
                    loggerProvider.getConsoleReader().setPrompt("");

                    try {
                        if (!commandManager.dispatchCommand(line))
                            loggerProvider.info(ReformCloudClient.getInstance().getInternalCloudNetwork().getLoaded().getHelp_command_not_found());
                    } catch (final Throwable throwable) {
                        StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while handling command line input", throwable);
                    }
                }
            }
        } catch (final Throwable throwable) {
            StringUtil.printError(loggerProvider, "Error while reading command line", throwable);
        }
    }
}
