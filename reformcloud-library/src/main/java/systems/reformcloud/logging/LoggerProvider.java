/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.logging;

import com.google.gson.JsonObject;
import jline.console.ConsoleReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.logging.enums.AnsiColourHandler;
import systems.reformcloud.logging.handlers.IConsoleInputHandler;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.runtime.Reload;
import systems.reformcloud.utility.runtime.Shutdown;
import systems.reformcloud.utility.time.DateProvider;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.logging.*;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

public class LoggerProvider extends AbstractLoggerProvider implements Serializable, AutoCloseable, Reload, Shutdown {
    private static final long serialVersionUID = 3076534030843453815L;

    /**
     * The current instance of the logger provider
     */
    public static Optional<LoggerProvider> instance;

    /**
     * The current console reader
     */
    private ConsoleReader consoleReader;

    /**
     * The log date format
     */
    private final DateFormat dateFormat = DateProvider.getDateFormat("MM/dd/yyyy HH:mm:ss");

    /**
     * The debug log file
     */
    protected final File debugLogFile = new File("reformcloud/logs/debug-" + System.currentTimeMillis() + ".log");

    /**
     * The logger handler of the cloud
     */
    protected final LoggerHandler loggerHandler = new LoggerHandler();

    /**
     * The current controller time
     */
    private long controllerTime = System.currentTimeMillis();

    /**
     * The current debug status
     */
    private boolean debug = false;

    /**
     * The registered logger handlers
     */
    private List<IConsoleInputHandler> iConsoleInputHandlers = new ArrayList<>();

    /**
     * Creates a new instance of the cloud logger
     *
     * @throws IOException      If an exception occurs while creating the console reader
     */
    public LoggerProvider() throws IOException {
        instance = Optional.of(this);
        AbstractLoggerProvider.globalInstance.set(this);

        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "ERROR");
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");

        try {
            this.consoleReader = new ConsoleReader(System.in, System.out);
            this.consoleReader.setExpandEvents(false);
        } catch (final IOException ignored) {
        }

        if (!Files.exists(Paths.get("reformcloud")))
            Files.createDirectory(Paths.get("reformcloud"));
        if (!Files.exists(Paths.get("reformcloud", "logs")))
            Files.createDirectory(Paths.get("reformcloud", "logs"));

        AnsiConsole.systemInstall();

        final File file = new File("reformcloud/", "logs/");
        Formatter formatter = new FormatterImpl();

        Handler fileHandler = new FileHandler(file.getCanonicalPath() + "/latest", Integer.MAX_VALUE, 8, true);
        fileHandler.setEncoding(StandardCharsets.UTF_8.name());
        fileHandler.setLevel(Level.ALL);
        fileHandler.setFormatter(formatter);
        loggerHandler.addHandler(fileHandler);

        new Worker();
    }

    private final LinkedBlockingQueue<Runnable> out = new LinkedBlockingQueue<>();

    public static AbstractLoggerProvider newSaveLogger() {
        try {
            return new LoggerProvider();
        } catch (final IOException ex) {
            return null;
        }
    }

    @Override
    public void info(String message) {
        out.add(() -> {
            loggerHandler.log(Level.INFO, AnsiColourHandler.stripColor(message));
            try {
                this.consoleReader.println(Ansi.ansi().eraseLine(
                        Ansi.Erase.ALL).toString() + ConsoleReader.RESET_LINE + AnsiColourHandler.toColouredString(StringUtil.LOGGER_INFO.replace("%date%", dateFormat.format(controllerTime)) + message) + Ansi.ansi().reset().toString());
                this.complete();

                this.handleAll(AnsiColourHandler.stripColor(message));
            } catch (final IOException ex) {
                StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while printing logging line", ex);
            }
        });
    }

    @Override
    public void warn(String message) {
        out.add(() -> {
            loggerHandler.log(Level.WARNING, AnsiColourHandler.stripColor(message));
            try {
                this.consoleReader.println(Ansi.ansi().eraseLine(
                        Ansi.Erase.ALL).toString() + ConsoleReader.RESET_LINE + AnsiColourHandler.toColouredString(StringUtil.LOGGER_WARN.replace("%date%", dateFormat.format(controllerTime)) + message) + Ansi.ansi().reset().toString());
                this.complete();

                this.handleAll(AnsiColourHandler.stripColor(message));
            } catch (final IOException ex) {
                StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while printing logging line", ex);
            }
        });
    }

    @Override
    public void serve(String message) {
        out.add(() -> {
            loggerHandler.log(Level.SEVERE, AnsiColourHandler.stripColor(message));
            try {
                this.consoleReader.println(Ansi.ansi().eraseLine(
                        Ansi.Erase.ALL).toString() + ConsoleReader.RESET_LINE + AnsiColourHandler.toColouredString(StringUtil.LOGGER_ERR.replace("%date%", dateFormat.format(controllerTime)) + message) + Ansi.ansi().reset().toString());
                this.complete();

                this.handleAll(AnsiColourHandler.stripColor(message));
            } catch (final IOException ex) {
                StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while printing logging line", ex);
            }
        });
    }

    @Override
    public void coloured(String message) {
        out.add(() -> {
            loggerHandler.log(Level.INFO, AnsiColourHandler.stripColor(message));
            try {
                this.consoleReader.println(Ansi.ansi().eraseLine(
                        Ansi.Erase.ALL).toString() + ConsoleReader.RESET_LINE + AnsiColourHandler.toColouredString(message) + Ansi.ansi().reset().toString());
                this.complete();

                this.handleAll(AnsiColourHandler.stripColor(message));
            } catch (final IOException ex) {
                StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while printing logging line", ex);
            }
        });
    }

    @Override
    public void exception(Throwable cause) {
        out.add(() -> {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(cause).append("\n");
            for (StackTraceElement stackTraceElement : cause.getStackTrace())
                stringBuilder.append("    at ").append(stackTraceElement).append("\n");

            for (Throwable suppressed : cause.getSuppressed())
                this.suppressedException(suppressed, cause.getStackTrace(), "Suppressed: ", "\t", stringBuilder);

            String exception = stringBuilder.substring(0, stringBuilder.length() - 2);

            this.serve(exception);
            this.handleAll(exception);
        });
    }

    private void suppressedException(Throwable cause,
                                     StackTraceElement[] enclosing,
                                     String caption,
                                     String prefix,
                                     StringBuilder stringBuilder) {
        StackTraceElement[] trace = cause.getStackTrace();
        int m = trace.length - 1;
        int n = enclosing.length - 1;
        while (m >= 0 && n >= 0 && trace[m].equals(enclosing[n]))
            m--;
        n--;

        int framesInCommon = trace.length - 1 - m;

        stringBuilder.append(prefix + caption + cause);
        for (int i = 0; i <= m; i++)
            stringBuilder.append(prefix + "\tat " + trace[i]);
        if (framesInCommon != 0)
            stringBuilder.append(prefix + "\t... " + framesInCommon + " more");

        for (Throwable se : cause.getSuppressed())
            suppressedException(se, cause.getStackTrace(), "", "\t", stringBuilder);

        Throwable ourCause = cause.getCause();
        if (ourCause != null)
            suppressedException(ourCause, trace, "Caused by: ", "", stringBuilder);
    }

    @Override
    public void handleAll(String message) {
        this.iConsoleInputHandlers.forEach(iConsoleInputHandler -> iConsoleInputHandler.handle(message));
    }

    @Override
    public String readLine() {
        try {
            return this.consoleReader.readLine();
        } catch (final IOException ex) {
            StringUtil.printError(this, "Error while reading console input", ex);
        }

        return "";
    }

    @Override
    public void clearScreen() {
        try {
            this.consoleReader.clearScreen();
            ReformCloudLibraryService.sendHeader(this);
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not clear screen", ex);
        }
    }

    @Override
    public LoggerProvider emptyLine() {
        try {
            this.consoleReader.println(" ");
            this.complete();

            this.handleAll("\n");
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while printing logging line", ex);
        }

        return this;
    }

    @Override
    public void write(String text) {
        out.add(() -> {
            try {
                String newText = AnsiColourHandler.toColouredString(text);
                this.consoleReader.println(Ansi.ansi().eraseLine(
                        Ansi.Erase.ALL).toString() + ConsoleReader.RESET_LINE + newText + Ansi.ansi().reset().toString());
                this.complete();
            } catch (final IOException ex) {
                StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while printing logging line", ex);
            }
        });
    }

    @Override
    public void debug(String msg) {
        out.add(() -> {
            try {
                if (this.debug) {
                    this.consoleReader.println(Ansi.ansi().eraseLine(
                            Ansi.Erase.ALL).toString() + ConsoleReader.RESET_LINE + AnsiColourHandler
                            .toColouredString("[§6" + this.dateFormat.format(this.controllerTime) + "§r] " + msg)
                            + Ansi.ansi().reset().toString());
                    this.complete();

                    this.handleAll(AnsiColourHandler.stripColor(msg));
                }

                if (!debugLogFile.exists())
                    debugLogFile.createNewFile();

                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(debugLogFile, true));
                bufferedWriter.append(msg + "\n");
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (final IOException ex) {
                StringUtil.printError(this, "Error while writing to debug log", ex);
            }
        });
    }

    @Override
    public void flush() {
        try {
            this.consoleReader.flush();
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while flushing logger", ex);
        }
    }

    @Override
    public void complete() {
        try {
            this.consoleReader.drawLine();
            this.consoleReader.flush();
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while printing logging line", ex);
        }
    }

    @Override
    public void finish() {
        try {
            instance = Optional.empty();
            this.finalize();
        } catch (final Throwable ignored) {
        }
    }

    @Override
    public void shutdownAll() {
        this.close();
    }

    @Deprecated
    @Override
    public void reloadAll() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void close() {
        instance = Optional.empty();
        AbstractLoggerProvider.globalInstance.set(null);

        try {
            this.consoleReader.drawLine();
            this.consoleReader.flush();

            for (Handler handler : loggerHandler.getHandlers())
                handler.close();

            this.consoleReader.killLine();
            this.consoleReader.close();
            this.iConsoleInputHandlers.clear();
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while closing logger", ex);
        }
    }

    @Override
    public void registerLoggerHandler(final IConsoleInputHandler iConsoleInputHandler) {
        this.iConsoleInputHandlers.add(iConsoleInputHandler);
    }

    @Override
    public String uploadLog(String input) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("https://paste.reformcloud.systems/documents");

        try {
            httpPost.setEntity(new StringEntity(AnsiColourHandler.stripColourCodes(input), ContentType.TEXT_PLAIN));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            final String result = EntityUtils.toString(httpResponse.getEntity());
            JsonObject jsonObject = ReformCloudLibraryService.PARSER.parse(result).getAsJsonObject();
            if (httpResponse.getStatusLine().getStatusCode() != 201 || jsonObject.has("message"))
                return "The following error occurred: " + jsonObject.get("message").getAsString();

            return "https://paste.reformcloud.systems/" + jsonObject.get("key").getAsString();
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while uploading log", ex);
        }

        return "Could not post log on \"paste.reformcloud.systems\"!";
    }

    @Override
    public Consumer<String> info() {
        return this::info;
    }

    @Override
    public Consumer<String> warn() {
        return this::warn;
    }

    @Override
    public Consumer<String> serve() {
        return this::serve;
    }

    @Override
    public Consumer<String> coloured() {
        return this::coloured;
    }

    @Override
    public Consumer<Throwable> exception() {
        return this::exception;
    }

    public ConsoleReader getConsoleReader() {
        return this.consoleReader;
    }

    public DateFormat getDateFormat() {
        return this.dateFormat;
    }

    public File getDebugLogFile() {
        return this.debugLogFile;
    }

    public LoggerHandler getLoggerHandler() {
        return this.loggerHandler;
    }

    public long getControllerTime() {
        return this.controllerTime;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public List<IConsoleInputHandler> getIConsoleInputHandlers() {
        return this.iConsoleInputHandlers;
    }

    public void setControllerTime(long controllerTime) {
        this.controllerTime = controllerTime;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    private final class LoggerHandler extends Logger {
        private LoggerHandler() {
            super("ReformCloudLogger", null);
            setLevel(Level.ALL);
            setUseParentHandlers(false);
        }
    }

    private class FormatterImpl extends Formatter {
        @Override
        public String format(LogRecord record) {
            StringBuilder stringBuilder = new StringBuilder();
            if (record.getThrown() != null) {
                StringWriter stringWriter = new StringWriter();
                record.getThrown().printStackTrace(new PrintWriter(stringWriter));
                stringBuilder.append(stringWriter).append("\n");
            }

            return ConsoleReader.RESET_LINE +
                    "[" +
                    dateFormat.format(record.getMillis()) +
                    "] " +
                    record.getLevel().getName() +
                    ": " +
                    formatMessage(record) +
                    "\n" +
                    stringBuilder.toString();
        }
    }

    private class Worker extends Thread {
        Worker() {
            setDaemon(true);
            setPriority(Thread.MIN_PRIORITY);
            start();
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    out.take().run();
                } catch (final InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}