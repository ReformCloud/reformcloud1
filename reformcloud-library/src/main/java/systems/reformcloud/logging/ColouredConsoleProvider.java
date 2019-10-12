/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.logging;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import eu.byteexception.requestbuilder.RequestBuilder;
import eu.byteexception.requestbuilder.method.RequestMethod;
import eu.byteexception.requestbuilder.result.RequestResult;
import eu.byteexception.requestbuilder.result.stream.StreamType;
import jline.console.ConsoleReader;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.logging.enums.AnsiColourHandler;
import systems.reformcloud.logging.handlers.ConsoleInputHandler;
import systems.reformcloud.utility.Require;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.annotiations.ForRemoval;
import systems.reformcloud.utility.files.DownloadManager;
import systems.reformcloud.utility.runtime.Reload;
import systems.reformcloud.utility.runtime.Shutdown;
import systems.reformcloud.utility.time.DateProvider;

import java.io.*;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.logging.*;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

public class ColouredConsoleProvider extends AbstractLoggerProvider implements Serializable, AutoCloseable,
    Reload, Shutdown {

    private static final long serialVersionUID = 3076534030843453815L;

    /**
     * The current instance of the logger provider
     */
    public static ColouredConsoleProvider instance;

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
    private final File debugLogFile = new File(
        "reformcloud/logs/debug-" + System.currentTimeMillis() + ".log");

    /**
     * The logger handler of the cloud
     */
    private final LoggerHandler loggerHandler = new LoggerHandler();

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
    private List<ConsoleInputHandler> consoleInputHandlers = new ArrayList<>();

    /**
     * Creates a new instance of the cloud logger
     *
     * @throws IOException If an exception occurs while creating the console reader
     */
    public ColouredConsoleProvider() throws IOException {
        instance = this;
        AbstractLoggerProvider.globalInstance.set(this);

        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "ERROR");
        System.setProperty("java.util.logging.SimpleFormatter.format",
            "[%1$tF %1$tT] [%4$-7s] %5$s %n");

        try {
            this.consoleReader = new ConsoleReader(System.in, System.out);
            this.consoleReader.setExpandEvents(false);
        } catch (final IOException ignored) {
        }

        if (!Files.exists(Paths.get("reformcloud"))) {
            Files.createDirectory(Paths.get("reformcloud"));
        }
        if (!Files.exists(Paths.get("reformcloud", "logs"))) {
            Files.createDirectory(Paths.get("reformcloud", "logs"));
        }

        AnsiConsole.systemInstall();

        final File file = new File("reformcloud/", "logs/");
        Formatter formatter = new FormatterImpl();

        Handler fileHandler = new FileHandler(file.getCanonicalPath() + "/latest",
            Integer.MAX_VALUE, 8, true);
        fileHandler.setEncoding(StandardCharsets.UTF_8.name());
        fileHandler.setLevel(Level.ALL);
        fileHandler.setFormatter(formatter);
        loggerHandler.addHandler(fileHandler);

        new Worker();
    }

    private final LinkedBlockingQueue<Runnable> out = new LinkedBlockingQueue<>();

    public static AbstractLoggerProvider newSaveLogger() {
        try {
            return new ColouredConsoleProvider();
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
                    Ansi.Erase.ALL).toString() + ConsoleReader.RESET_LINE + AnsiColourHandler
                    .toColouredString(
                        StringUtil.LOGGER_INFO.replace("%date%", dateFormat.format(controllerTime))
                            + message) + Ansi.ansi().reset().toString());
                this.complete();

                this.handleAll(AnsiColourHandler.stripColor(message));
            } catch (final IOException ex) {
                StringUtil
                    .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                        "Error while printing logging line", ex);
            }
        });
    }

    @Override
    public void warn(String message) {
        out.add(() -> {
            loggerHandler.log(Level.WARNING, AnsiColourHandler.stripColor(message));
            try {
                this.consoleReader.println(Ansi.ansi().eraseLine(
                    Ansi.Erase.ALL).toString() + ConsoleReader.RESET_LINE + AnsiColourHandler
                    .toColouredString(
                        StringUtil.LOGGER_WARN.replace("%date%", dateFormat.format(controllerTime))
                            + message) + Ansi.ansi().reset().toString());
                this.complete();

                this.handleAll(AnsiColourHandler.stripColor(message));
            } catch (final IOException ex) {
                StringUtil
                    .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                        "Error while printing logging line", ex);
            }
        });
    }

    @Override
    public void serve(String message) {
        out.add(() -> {
            loggerHandler.log(Level.SEVERE, AnsiColourHandler.stripColor(message));
            try {
                this.consoleReader.println(Ansi.ansi().eraseLine(
                    Ansi.Erase.ALL).toString() + ConsoleReader.RESET_LINE + AnsiColourHandler
                    .toColouredString(
                        StringUtil.LOGGER_ERR.replace("%date%", dateFormat.format(controllerTime))
                            + message) + Ansi.ansi().reset().toString());
                this.complete();

                this.handleAll(AnsiColourHandler.stripColor(message));
            } catch (final IOException ex) {
                StringUtil
                    .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                        "Error while printing logging line", ex);
            }
        });
    }

    @Override
    public void coloured(String message) {
        out.add(() -> {
            loggerHandler.log(Level.INFO, AnsiColourHandler.stripColor(message));
            try {
                this.consoleReader.println(Ansi.ansi().eraseLine(
                    Ansi.Erase.ALL).toString() + ConsoleReader.RESET_LINE + AnsiColourHandler
                    .toColouredString(message) + Ansi.ansi().reset().toString());
                this.complete();

                this.handleAll(AnsiColourHandler.stripColor(message));
            } catch (final IOException ex) {
                StringUtil
                    .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                        "Error while printing logging line", ex);
            }
        });
    }

    @Override
    public void exception(Throwable cause) {
        Require.requireNotNull(cause);
        out.add(() -> {
            StringBuilder stringBuilder = new StringBuilder();
            StringWriter stringWriter = new StringWriter();

            cause.printStackTrace(new PrintWriter(stringWriter));
            stringBuilder.append(stringWriter);

            String caused = stringBuilder.toString();

            this.serve(caused);
            this.handleAll(caused);
        });
    }

    @Override
    public void handleAll(String message) {
        this.consoleInputHandlers
            .forEach(consoleInputHandler -> consoleInputHandler.handle(message));
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
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Could not clear screen", ex);
        }
    }

    @Override
    public ColouredConsoleProvider emptyLine() {
        out.add(() -> {
            try {
                this.consoleReader.println(String.valueOf(ConsoleReader.RESET_LINE));
                this.complete();

                this.handleAll("\n");
            } catch (final IOException ex) {
                StringUtil
                    .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                        "Error while printing logging line", ex);
            }
        });

        return this;
    }

    @Override
    public void write(String text) {
        out.add(() -> {
            try {
                String newText = AnsiColourHandler.toColouredString(text);
                this.consoleReader.println(Ansi.ansi().eraseLine(
                    Ansi.Erase.ALL).toString() + ConsoleReader.RESET_LINE + newText + Ansi.ansi()
                    .reset().toString());
                this.complete();
            } catch (final IOException ex) {
                StringUtil
                    .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                        "Error while printing logging line", ex);
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
                        .toColouredString(
                            "[§6" + this.dateFormat.format(this.controllerTime) + "§r] " + msg)
                        + Ansi.ansi().reset().toString());
                    this.complete();

                    this.handleAll(AnsiColourHandler.stripColor(msg));

                    if (!debugLogFile.exists()) {
                        debugLogFile.createNewFile();
                    }

                    BufferedWriter bufferedWriter = new BufferedWriter(
                        new FileWriter(debugLogFile, true));
                    bufferedWriter.append(msg).append("\n");
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
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
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Error while flushing logger", ex);
        }
    }

    @Override
    public void complete() {
        try {
            this.consoleReader.drawLine();
            this.consoleReader.flush();
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Error while printing logging line", ex);
        }
    }

    @Override
    public void finish() {
        try {
            instance = null;
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
        instance = null;
        AbstractLoggerProvider.globalInstance.set(null);

        try {
            this.consoleReader.drawLine();
            this.consoleReader.flush();

            for (Handler handler : loggerHandler.getHandlers()) {
                handler.close();
            }

            this.consoleReader.killLine();
            this.consoleReader.close();
            this.consoleInputHandlers.clear();
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Error while closing logger", ex);
        }
    }

    @Override
    public void registerLoggerHandler(final ConsoleInputHandler consoleInputHandler) {
        this.consoleInputHandlers.add(consoleInputHandler);
    }

    @Override
    public String uploadLog(String input) {
        try {
            final RequestResult requestResult = RequestBuilder.newBuilder("https://paste.reformcloud.systems/documents", Proxy.NO_PROXY)
                .setRequestMethod(RequestMethod.POST)
                .addHeader(DownloadManager.REQUEST_PROPERTY.getFirst(), DownloadManager.REQUEST_PROPERTY.getSecond())
                .enableOutput()
                .fireAndForget();

            try (DataOutputStream dataOutputStream =
                     new DataOutputStream(requestResult.getOutputStream())) {
                dataOutputStream.writeBytes(input);
                dataOutputStream.flush();
            }

            JsonObject jsonObject;
            try (JsonReader jsonReader =
                     new JsonReader(new InputStreamReader(requestResult.getStream(StreamType.DEFAULT)))) {
                jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
            }

            if (jsonObject == null) {
                return "Could not post log on \"paste.reformcloud.systems\"!";
            }

            if (jsonObject.has("message")) {
                return "An error occurred: " + jsonObject.get("message").getAsString();
            }

            Configuration configuration = new Configuration(jsonObject);
            requestResult.forget();
            return "https://paste.reformcloud.systems/" + configuration.getStringValue("key");
        } catch (final IOException ex) {
            StringUtil.printError(
                ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                "Could not post log on \"paste.reformcloud.systems\"!",
                ex
            );
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

    @Override
    public ConsoleReader consoleReader() {
        return this.consoleReader;
    }

    @Deprecated
    @ForRemoval
    public ConsoleReader getConsoleReader() {
        return this.consoleReader;
    }

    @Override
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

    @Override
    public boolean isDebug() {
        return this.debug;
    }

    public List<ConsoleInputHandler> getIConsoleInputHandlers() {
        return this.consoleInputHandlers;
    }

    @Override
    public void setControllerTime(long controllerTime) {
        this.controllerTime = controllerTime;
    }

    @Override
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