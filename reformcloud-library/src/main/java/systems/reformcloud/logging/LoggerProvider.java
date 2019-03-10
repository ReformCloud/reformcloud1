/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.logging;

import jline.console.ConsoleReader;
import lombok.Getter;
import lombok.Setter;
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
import systems.reformcloud.configurations.Configuration;
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
import java.util.Arrays;
import java.util.List;
import java.util.logging.*;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

@Getter
public class LoggerProvider extends Logger implements Serializable, AutoCloseable, Reload, Shutdown {
    private static final long serialVersionUID = 3076534030843453815L;

    private ConsoleReader consoleReader;
    private final DateFormat dateFormat = DateProvider.getDateFormat("MM/dd/yyyy HH:mm:ss");

    protected final File debugLogFile = new File("klarcloud/logs/debug-" + System.currentTimeMillis() + ".log");

    @Setter
    private long controllerTime = System.currentTimeMillis();

    @Setter
    private boolean debug = false;

    private List<IConsoleInputHandler> iConsoleInputHandlers = new ArrayList<>();

    /**
     * Creates a new instance of the {@link LoggerProvider}
     *
     * @throws IOException
     */
    public LoggerProvider() throws IOException {
        super("ReformLogger", null);

        setLevel(Level.ALL);
        setUseParentHandlers(false);

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
        final SimpleFormatter simpleFormatter = new SimpleFormatter();

        FileHandler fileHandler = new FileHandler(file.getCanonicalPath() + "/CloudLog", 5242880, 100, false);
        fileHandler.setEncoding(StandardCharsets.UTF_8.name());
        fileHandler.setLevel(Level.ALL);
        fileHandler.setFormatter(simpleFormatter);

        addHandler(fileHandler);
    }

    /**
     * Print an info message to the console
     *
     * @param message
     */
    public void info(String message) {
        super.log(Level.INFO, AnsiColourHandler.stripColor(message));
        try {
            this.consoleReader.println(Ansi.ansi().eraseLine(
                    Ansi.Erase.ALL).toString() + ConsoleReader.RESET_LINE + AnsiColourHandler.toColouredString(StringUtil.LOGGER_INFO.replace("%date%", dateFormat.format(controllerTime)) + message) + Ansi.ansi().reset().toString());
            this.complete();

            this.handleAll(AnsiColourHandler.stripColor(message));
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while printing logging line", ex);
        }
    }

    /**
     * Prints a warning to the console
     *
     * @param message
     */
    public void warn(String message) {
        super.log(Level.WARNING, AnsiColourHandler.stripColor(message));
        try {
            this.consoleReader.println(Ansi.ansi().eraseLine(
                    Ansi.Erase.ALL).toString() + ConsoleReader.RESET_LINE + AnsiColourHandler.toColouredString(StringUtil.LOGGER_WARN.replace("%date%", dateFormat.format(controllerTime)) + message) + Ansi.ansi().reset().toString());
            this.complete();

            this.handleAll(AnsiColourHandler.stripColor(message));
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while printing logging line", ex);
        }
    }

    /**
     * Prints an error to the console
     *
     * @param message
     */
    public void serve(String message) {
        super.log(Level.SEVERE, AnsiColourHandler.stripColor(message));
        try {
            this.consoleReader.println(Ansi.ansi().eraseLine(
                    Ansi.Erase.ALL).toString() + ConsoleReader.RESET_LINE + AnsiColourHandler.toColouredString(StringUtil.LOGGER_ERR.replace("%date%", dateFormat.format(controllerTime)) + message) + Ansi.ansi().reset().toString());
            this.complete();

            this.handleAll(AnsiColourHandler.stripColor(message));
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while printing logging line", ex);
        }
    }

    public void coloured(String message) {
        super.log(Level.INFO, AnsiColourHandler.stripColor(message));
        try {
            this.consoleReader.println(Ansi.ansi().eraseLine(
                    Ansi.Erase.ALL).toString() + ConsoleReader.RESET_LINE + AnsiColourHandler.toColouredString(message) + Ansi.ansi().reset().toString());
            this.complete();

            this.handleAll(AnsiColourHandler.stripColor(message));
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while printing logging line", ex);
        }
    }

    public void exception(Throwable cause) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(cause.getMessage()).append("\n");
        Arrays.stream(cause.getStackTrace()).forEach(e -> stringBuilder.append("    " + e).append("\n"));
        this.serve(stringBuilder.substring(0));

        this.handleAll(stringBuilder.substring(0));
    }

    /**
     * Handel all registered {@link IConsoleInputHandler}
     *
     * @param message
     */
    private void handleAll(String message) {
        this.iConsoleInputHandlers.forEach(iConsoleInputHandler -> iConsoleInputHandler.handle(message));
    }

    public String readLine() {
        try {
            return this.consoleReader.readLine();
        } catch (final IOException ex) {
            StringUtil.printError(this, "Error while reading console input", ex);
        }

        return "";
    }

    /**
     * Clears the screen with the {@link ConsoleReader}
     */
    public void clearScreen() {
        try {
            this.consoleReader.clearScreen();
            ReformCloudLibraryService.sendHeader(this);
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not clear screen", ex);
        }
    }

    /**
     * Prints an empty line to the console
     *
     * @return this
     */
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

    public void write(String text) {
        try {
            text = AnsiColourHandler.toColouredString(text);
            this.consoleReader.println(Ansi.ansi().eraseLine(
                    Ansi.Erase.ALL).toString() + ConsoleReader.RESET_LINE + AnsiColourHandler.toColouredString(text) + Ansi.ansi().reset().toString());
            this.complete();
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while printing logging line", ex);
        }
    }

    public void debug(String msg) {
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
    }

    /**
     * Flush the {@link ConsoleReader}
     */
    public void flush() {
        try {
            this.consoleReader.flush();
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while flushing logger", ex);
        }
    }

    /**
     * Complete the print line
     * <p>
     * {@link ConsoleReader}
     */
    public void complete() {
        try {
            this.consoleReader.drawLine();
            this.consoleReader.flush();
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while printing logging line", ex);
        }
    }

    /**
     * Closes the {@link ConsoleReader} and closes all {@link Handler}
     */
    public void shutdownAll() {
        this.close();
    }

    /**
     * Not implemented yet
     */
    @Deprecated
    @Override
    public void reloadAll() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see LoggerProvider#shutdownAll()
     */
    @Override
    public void close() {
        try {
            this.consoleReader.drawLine();
            this.consoleReader.flush();

            for (Handler handler : this.getHandlers())
                handler.close();

            this.consoleReader.killLine();
            this.consoleReader.close();
            this.iConsoleInputHandlers.clear();
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while closing logger", ex);
        }
    }

    public void registerLoggerHandler(final IConsoleInputHandler iConsoleInputHandler) {
        this.iConsoleInputHandlers.add(iConsoleInputHandler);
    }

    public String uploadLog(String input) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("https://paste.reformcloud.systems/documents");

        try {
            httpPost.setEntity(new StringEntity(new Configuration()
                    .addStringProperty("text", input).getJsonString(), ContentType.APPLICATION_JSON));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            final String result = EntityUtils.toString(httpResponse.getEntity());

            return "https://paste.reformcloud.systems/" + ReformCloudLibraryService.PARSER
                    .parse(result).getAsJsonObject().get("key").getAsString();
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while uploading log", ex);
        }

        return "Could not post log on \"paste.reformcloud.de\"!";
    }
}