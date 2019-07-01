/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.launcher;

import io.netty.util.ResourceLeakDetector;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.commands.CommandManager;
import systems.reformcloud.logging.ColouredConsoleProvider;
import systems.reformcloud.logging.console.ReformAsyncConsole;
import systems.reformcloud.logging.console.thread.DefaultInfinitySleeper;
import systems.reformcloud.logging.console.thread.InfinitySleeper;
import systems.reformcloud.utility.ExitUtil;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.files.FileUtils;
import systems.reformcloud.utility.time.DateProvider;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

final class ReformCloudLauncher implements Serializable {

    /**
     * Main method of ReformCloudController
     *
     * @param args The start parameters of reformcloud
     * @throws Throwable Will be thrown if any exception occurs
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

        final InfinitySleeper infinitySleeper = new DefaultInfinitySleeper();

        System.out.println("\nTrying to startup ReformCloudController...");
        System.out.println("Startup time: " + DateProvider.formatByDefaultFormat(current) + "\n");

        if (Files.exists(Paths.get("reformcloud/logs"))) {
            FileUtils.deleteFullDirectory(Paths.get("reformcloud/logs"));
        }

        final CommandManager commandManager = new CommandManager();
        final ColouredConsoleProvider colouredConsoleProvider = new ColouredConsoleProvider();

        ReformCloudLibraryService.sendHeader(colouredConsoleProvider);

        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);

        colouredConsoleProvider.setDebug(options.contains("--debug"));
        new ReformCloudController(colouredConsoleProvider, commandManager, options.contains("--ssl"),
            current);

        colouredConsoleProvider
            .info(ReformCloudController.getInstance().getLoadedLanguage().getHelp_default());
        new ReformAsyncConsole(colouredConsoleProvider, commandManager, "Controller");

        infinitySleeper.sleep();
    }
}