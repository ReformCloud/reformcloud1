/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.examples.logging;

import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.logging.enums.AnsiColourHandler;

import java.io.IOException;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

public class LoggerProviderImplementationExample extends LoggerProvider {
    /**
     * Creates a new instance of the {@link LoggerProvider}
     *
     * @param colour
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws IOException
     */

    /**
     * Creates a new ReformCloud Logger instance
     */
    public LoggerProviderImplementationExample() throws IOException {
        super();
    }

    /**
     * Prints a info String
     */
    @Override
    public void info(String message) {
        super.info(message);
    }

    @Override
    public void serve(String message) {
        try {
            this.getConsoleReader() //Returns the ConsoleReader of the Cloud
                    .println(AnsiColourHandler.toColouredString("§cI am red §rI have the default colour, because of a reset :("
                            + "§eI am yellow")); //Like Bukkit and BungeeCord colour codes
            this.complete(); //Needed
        } catch (final IOException ignored) {
        }

        super.serve(message);
    }
}
