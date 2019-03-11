/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.examples.logging;

import systems.reformcloud.logging.LoggerProvider;

import java.io.IOException;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

public class LoggingExample {
    public LoggingExample() throws IOException {
        final LoggerProvider loggerProvider = new LoggerProvider();

        loggerProvider.info("Hello boy"); //Sends a INFO message into the console
        loggerProvider.warn("Warning"); //Sends a WARNING message into the console
        loggerProvider.serve("Error"); //Sends a ERROR message into the console
        loggerProvider.debug("Test debug"); //Sends a debug message into the console if debug is enabled
        //and writes it into the debug log
    }
}
