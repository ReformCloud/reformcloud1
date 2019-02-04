/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.launcher;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.commands.CommandManager;
import systems.reformcloud.libloader.LibraryLoader;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.files.FileUtils;
import systems.reformcloud.utility.time.DateProvider;
import io.netty.util.ResourceLeakDetector;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

final class ReformCloudLauncher {
    /**
     * Main method of ReformCloudController
     *
     * @param args
     * @throws Throwable
     */
    public static synchronized void main(String[] args) throws Throwable {
        final long current = System.currentTimeMillis();

        System.out.println("\nTrying to startup ReformCloudController...");
        System.out.println("Startup time: " + DateProvider.formatByDefaultFormat(current) + "\n");

        new LibraryLoader().loadJarFileAndInjectLibraries();

        if (Files.exists(Paths.get("reformcloud/logs")))
            FileUtils.deleteFullDirectory(Paths.get("reformcloud/logs"));

        System.out.println();

        final CommandManager commandManager = new CommandManager();
        final LoggerProvider loggerProvider = new LoggerProvider();

        ReformCloudLibraryService.sendHeader(loggerProvider);

        final List<String> options = Arrays.asList(args);

        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);

        new ReformCloudController(loggerProvider, commandManager, options.contains("--ssl"), current);

        loggerProvider.info(ReformCloudController.getInstance().getLoadedLanguage().getHelp_default());

        String line;
        try {
            while (true) {
                loggerProvider.getConsoleReader().setPrompt("");
                loggerProvider.getConsoleReader().resetPromptLine("", "", 0);

                while ((line = loggerProvider.getConsoleReader().readLine(StringUtil.REFORM_VERSION + "-" + StringUtil.REFORM_SPECIFICATION + "@ReformCloudController > ")) != null && ReformCloudController.RUNNING) {
                    loggerProvider.getConsoleReader().setPrompt("");

                    if (!commandManager.dispatchCommand(line))
                        loggerProvider.info(ReformCloudController.getInstance().getLoadedLanguage().getHelp_command_not_found());
                    else
                        ReformCloudController.getInstance().getStatisticsProvider().addConsoleCommand();
                }
            }
        } catch (final Throwable throwable) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while handling command input", throwable);
        }
    }
}