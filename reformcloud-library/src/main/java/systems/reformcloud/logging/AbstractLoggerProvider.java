/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.logging;

import jline.console.ConsoleReader;
import systems.reformcloud.logging.handlers.IConsoleInputHandler;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * @author _Klaro | Pasqual K. / created on 14.04.2019
 */

public abstract class AbstractLoggerProvider implements Serializable {
    /**
     * The global logging instance
     */
    static final AtomicReference<AbstractLoggerProvider> globalInstance = new AtomicReference<>();

    /**
     * Infos a message to the console
     *
     * @param message The message which should be sent
     */
    public abstract void info(String message);

    /**
     * Warns a message to the console
     *
     * @param message       The message which should be sent
     */
    public abstract void warn(String message);

    /**
     * Serves a message to the console
     *
     * @param message       The message which should be sent
     */
    public abstract void serve(String message);

    /**
     * Colours a string by replacing the colour code
     *
     * @param message   The message which should be formatted
     */
    public abstract void coloured(String message);

    /**
     * Handles an exception to the console
     *
     * @param cause     The exception which occurs
     */
    public abstract void exception(Throwable cause);

    /**
     * Sends a message to all registered console handlers
     *
     * @param message       The message which was sent in the console
     */
    public abstract void handleAll(String message);

    /**
     * Reads the console line
     *
     * @return The read line of the console
     */
    public abstract String readLine();

    /**
     * Clears the screen
     */
    public abstract void clearScreen();

    /**
     * Sends an empty line to the console
     *
     * @return The instance of the provider
     */
    public abstract AbstractLoggerProvider emptyLine();

    /**
     * Writes a message to the console
     *
     * @param message       The message which should be written
     */
    public abstract void write(String message);

    /**
     * Debugs a message into the console and the debug file
     *
     * @param message       The message which should be debugged
     */
    public abstract void debug(String message);

    /**
     * Flushes the console reader
     */
    public abstract void flush();

    /**
     * Draws the current line and flushes the console reader
     */
    public abstract void complete();

    /**
     * Finalize the class and set the instance to nothing
     */
    public abstract void finish();

    /**
     * Registers a logger provider
     *
     * @param iConsoleInputHandler      The logger handler which should be registered
     */
    public abstract void registerLoggerHandler(IConsoleInputHandler iConsoleInputHandler);

    /**
     * Uploads a log to the reformcloud web server
     *
     * @param message       The log which should be uploaded
     * @return The url of the log
     */
    public abstract String uploadLog(String message);

    /**
     * Creates a consumer for the info method
     *
     * @return A consumer for the info method
     */
    public abstract Consumer<String> info();

    /**
     * Creates a consumer for the warn method
     *
     * @return A consumer for the warn method
     */
    public abstract Consumer<String> warn();

    /**
     * Creates a consumer for the serve method
     *
     * @return A consumer for the serve method
     */
    public abstract Consumer<String> serve();

    /**
     * Creates a consumer for the coloured method
     *
     * @return A consumer for the coloured method
     */
    public abstract Consumer<String> coloured();

    /**
     * Creates a consumer for the exception method
     *
     * @return A consumer for the exception method
     */
    public abstract Consumer<Throwable> exception();

    /**
     * Get the current console reader of the cloud
     *
     * @return the current console reader of the cloud
     */
    public abstract ConsoleReader consoleReader();

    /**
     * Creates a new logger
     *
     * @return Creates a new logger or returns the current logger instance
     */
    public static AbstractLoggerProvider defaultLogger() {
        return LoggerProvider.instance.isPresent() ? LoggerProvider.instance.get() : LoggerProvider.newSaveLogger();
    }
}
