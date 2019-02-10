/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility;

import systems.reformcloud.DiscordAddon;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.logging.enums.AnsiColourHandler;
import systems.reformcloud.logging.handlers.IConsoleInputHandler;

import java.io.Serializable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author _Klaro | Pasqual K. / created on 10.02.2019
 */

public final class ConsoleWriter implements Serializable, Runnable, IConsoleInputHandler {
    private Queue<String> consoleMessages = new ConcurrentLinkedDeque<>();

    public ConsoleWriter() {
        ReformCloudController.getInstance().getLoggerProvider().registerLoggerHandler(this);
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (!consoleMessages.isEmpty()
                    && DiscordAddon.getInstance().getJda() != null
                    && DiscordAddon.getInstance().getTextChannel() != null) {
                StringBuilder stringBuilder = new StringBuilder();
                while (!consoleMessages.isEmpty() && stringBuilder.length() < 1995) {
                    String message = consoleMessages.poll();
                    if (message.length() + stringBuilder.length() < 1995)
                        stringBuilder.append(message).append("\n");
                    else {
                        consoleMessages.offer(message);
                        break;
                    }
                }

                if (stringBuilder.length() != -1 && stringBuilder.length() != 0)
                    DiscordAddon.getInstance().getTextChannel().sendMessage(stringBuilder.substring(0)).queue();
            }

            try {
                Thread.sleep(2000);
            } catch (final InterruptedException ignored) {
            }
        }
    }

    @Override
    public void handle(String message) {
        consoleMessages.offer(AnsiColourHandler.stripColourCodes(message));
    }
}
