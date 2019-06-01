/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.logging.console;

import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.commands.AbstractCommandManager;
import systems.reformcloud.logging.AbstractLoggerProvider;
import systems.reformcloud.utility.StringUtil;

import java.beans.ConstructorProperties;
import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 27.05.2019
 */

public final class ReformAsyncConsole extends Thread implements Serializable {

    @ConstructorProperties({"abstractLoggerProvider", "commandManager", "buffer"})
    public ReformAsyncConsole(AbstractLoggerProvider abstractLoggerProvider, AbstractCommandManager commandManager, String buffer) {
        this.abstractLoggerProvider = abstractLoggerProvider;
        this.buffer = buffer;
        this.commandManager = commandManager;

        setDaemon(true);
        setPriority(Thread.MIN_PRIORITY);
        start();
    }

    private volatile boolean running = true;

    private final AbstractLoggerProvider abstractLoggerProvider;

    private final AbstractCommandManager commandManager;

    private final String buffer;

    @Override
    public void run() {
        String line;

        while (!isInterrupted()) {
            try {
                abstractLoggerProvider.consoleReader().setPrompt("");
                abstractLoggerProvider.consoleReader().resetPromptLine("", "", 0);

                while ((line = abstractLoggerProvider.consoleReader().readLine(StringUtil.REFORM_VERSION + "-"
                        + StringUtil.REFORM_SPECIFICATION + "@" + buffer + ":~# ")) != null && !line.trim().isEmpty() && running) {
                    abstractLoggerProvider.consoleReader().setPrompt("");

                    if (!commandManager.dispatchCommand(line)) {
                        abstractLoggerProvider.info().accept(ReformCloudLibraryServiceProvider.getInstance()
                                .getInternalCloudNetwork().getLoaded().getHelp_command_not_found());
                    }
                }
            } catch (final Throwable throwable) {
                abstractLoggerProvider.exception().accept(throwable);
            }
        }
    }

    @Override
    public void interrupt() {
        running = false;
        super.interrupt();
    }
}
