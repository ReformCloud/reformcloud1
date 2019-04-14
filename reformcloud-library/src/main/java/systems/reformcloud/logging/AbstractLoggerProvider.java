/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.logging;

import systems.reformcloud.logging.handlers.IConsoleInputHandler;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * @author _Klaro | Pasqual K. / created on 14.04.2019
 */

public abstract class AbstractLoggerProvider implements Serializable {
    public static final AtomicReference<AbstractLoggerProvider> globalInstance = new AtomicReference<>();

    public abstract void info(String message);

    public abstract void warn(String message);

    public abstract void serve(String message);

    public abstract void coloured(String message);

    public abstract void exception(Throwable cause);

    public abstract void handleAll(String message);

    public abstract String readLine();

    public abstract void clearScreen();

    public abstract AbstractLoggerProvider emptyLine();

    public abstract void write(String message);

    public abstract void debug(String message);

    public abstract void flush();

    public abstract void complete();

    public abstract void finish();

    public abstract void registerLoggerHandler(IConsoleInputHandler iConsoleInputHandler);

    public abstract String uploadLog(String message);

    public abstract Consumer<String> info();

    public abstract Consumer<String> warn();

    public abstract Consumer<String> serve();

    public abstract Consumer<String> coloured();

    public abstract Consumer<Throwable> exception();

    public AbstractLoggerProvider defaultLogger() {
        return LoggerProvider.instance.isPresent() ? LoggerProvider.instance.get() : LoggerProvider.newSaveLogger();
    }
}
