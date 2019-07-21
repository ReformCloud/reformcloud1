/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.configuration;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.logging.AbstractLoggerProvider;
import systems.reformcloud.utility.ExitUtil;
import systems.reformcloud.utility.Require;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.cloudsystem.EthernetAddress;
import systems.reformcloud.utility.files.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Predicate;

/**
 * @author _Klaro | Pasqual K. / created on 24.10.2018
 */

public final class CloudConfiguration implements Serializable {

    private String controllerKey;

    private String controllerIP;

    private String clientName;

    private String startIP;

    private int memory;

    private int controllerPort;

    private int controllerWebPort;

    private int logSize;

    private double cpu;

    private EthernetAddress ethernetAddress;

    /**
     * Creates or/and loads the Client Configuration
     */
    public CloudConfiguration(final boolean reload) {
        if (!Files.exists(Paths.get("configuration.properties")) && !Files
            .exists(Paths.get("ControllerKEY"))) {
            ReformCloudClient.getInstance().getColouredConsoleProvider().serve(
                "Please copy the \"ControllerKEY\" file in the root directory of the client");
            ReformCloudLibraryService.sleep(2000);
            System.exit(ExitUtil.CONTROLLERKEY_MISSING);
            return;
        } else if (Files.exists(Paths.get("ControllerKEY"))) {
            this.createDirectories();
            FileUtils.copyFile("ControllerKEY", "reformcloud/files/ControllerKEY");
            FileUtils.deleteFileIfExists(Paths.get("ControllerKEY"));
        }

        if (!reload) {
            this.clearProxyTemp();
            this.clearServerTemp();
        }

        this.defaultInit();
        this.load();
    }

    private void createDirectories() {
        for (File dir : new File[]{
            new File("reformcloud/templates/proxies"),
            new File("reformcloud/templates/servers"),
            new File("reformcloud/default/proxies/plugins"),
            new File("reformcloud/default/servers/plugins"),
            new File("reformcloud/temp/servers"),
            new File("reformcloud/temp/proxies"),
            new File("reformcloud/files"),
            new File("reformcloud/jars"),
            new File("reformcloud/apis"),
            new File("reformcloud/static/servers"),
            new File("reformcloud/static/proxies"),
            new File("reformcloud/saves/servers/logs"),
            new File("reformcloud/saves/proxies/logs")
        }) {
            Require.isTrue(dir.mkdirs(), "Could not create directory " + dir.getName());
        }
    }

    /**
     * Creates default Configuration or returns if the Configuration already exists
     */
    private void defaultInit() {
        if (Files.exists(Paths.get("configuration.properties"))) {
            return;
        }

        AbstractLoggerProvider colouredConsoleProvider =
            ReformCloudClient.getInstance().getColouredConsoleProvider();

        colouredConsoleProvider.info("Please provide the internal ReformCloudClient ip");
        String ip = this.readString(colouredConsoleProvider, s -> s.split("\\.").length == 4);
        colouredConsoleProvider.info("Please provide the Controller IP");
        String controllerIP = this.readString(colouredConsoleProvider, s -> s.split("\\.").length == 4);

        ReformCloudClient.getInstance().getColouredConsoleProvider()
            .info("Please provide the name of this Client. (Recommended: Client-01)");
        String clientID = this.readString(colouredConsoleProvider, s -> true);

        Properties properties = new Properties();

        properties.setProperty("controller.ip", controllerIP);
        properties.setProperty("controller.port", 5000 + StringUtil.EMPTY);
        properties.setProperty("controller.web-port", 4790 + StringUtil.EMPTY);

        properties.setProperty("general.client-name", clientID);
        properties.setProperty("general.start-host", ip);
        properties.setProperty("general.memory", 1024 + StringUtil.EMPTY);
        properties.setProperty("general.maxcpuusage", 90.00 + StringUtil.EMPTY);
        properties.setProperty("general.max-log-size", Integer.toString(46));

        try (OutputStream outputStream = Files
            .newOutputStream(Paths.get("configuration.properties"))) {
            properties.store(outputStream, "ReformCloud default Configuration");
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Could not store configuration.properties", ex);
        }

    }

    private void clearServerTemp() {
        final File dir = new File("reformcloud/temp/servers");

        if (dir.isDirectory()) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                if (file.isDirectory()) {
                    FileUtils.deleteFullDirectory(file.toPath());
                }
            }
        }
    }

    private void clearProxyTemp() {
        final File dir = new File("reformcloud/temp/proxies");

        if (dir.isDirectory()) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                if (file.isDirectory()) {
                    FileUtils.deleteFullDirectory(file.toPath());
                }
            }
        }
    }

    /**
     * Loads the CloudConfiguration
     */
    private void load() {
        Properties properties = new Properties();
        try (InputStreamReader inputStreamReader = new InputStreamReader(
            Files.newInputStream(Paths.get("configuration.properties")))) {
            properties.load(inputStreamReader);
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Could not load configuration.properties", ex);
        }

        this.clientName = properties.getProperty("general.client-name");
        this.startIP = properties.getProperty("general.start-host");
        this.memory = Integer.parseInt(properties.getProperty("general.memory"));

        this.controllerIP = properties.getProperty("controller.ip");
        this.controllerPort = Integer.parseInt(properties.getProperty("controller.port"));
        this.controllerWebPort = Integer.parseInt(properties.getProperty("controller.web-port"));
        this.cpu = Double.parseDouble(properties.getProperty("general.maxcpuusage"));
        this.logSize = Integer.parseInt(properties.getProperty("general.max-log-size", "46"));

        this.ethernetAddress = new EthernetAddress(this.controllerIP, this.controllerPort);

        this.controllerKey = FileUtils
            .readFileAsString(new File("reformcloud/files/ControllerKEY"));
    }

    private String readString(final AbstractLoggerProvider colouredConsoleProvider, Predicate<String> checkable) {
        String readLine = colouredConsoleProvider.readLine();
        while (readLine == null || !checkable.test(readLine) || readLine.trim().isEmpty()) {
            colouredConsoleProvider.info("Input invalid, please try again");
            readLine = colouredConsoleProvider.readLine();
        }

        return readLine;
    }

    public String getControllerKey() {
        return this.controllerKey;
    }

    public String getControllerIP() {
        return this.controllerIP;
    }

    public String getClientName() {
        return this.clientName;
    }

    public String getStartIP() {
        return this.startIP;
    }

    public int getMemory() {
        return this.memory;
    }

    public int getControllerWebPort() {
        return this.controllerWebPort;
    }

    public int getLogSize() {
        return this.logSize;
    }

    public double getCpu() {
        return this.cpu;
    }

    public EthernetAddress getEthernetAddress() {
        return this.ethernetAddress;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CloudConfiguration)) {
            return false;
        }
        final CloudConfiguration other = (CloudConfiguration) o;
        final Object this$controllerKey = this.getControllerKey();
        final Object other$controllerKey = other.getControllerKey();
        if (!Objects.equals(this$controllerKey, other$controllerKey)) {
            return false;
        }
        final Object this$controllerIP = this.getControllerIP();
        final Object other$controllerIP = other.getControllerIP();
        if (!Objects.equals(this$controllerIP, other$controllerIP)) {
            return false;
        }
        final Object this$clientName = this.getClientName();
        final Object other$clientName = other.getClientName();
        if (!Objects.equals(this$clientName, other$clientName)) {
            return false;
        }
        final Object this$startIP = this.getStartIP();
        final Object other$startIP = other.getStartIP();
        if (!Objects.equals(this$startIP, other$startIP)) {
            return false;
        }
        if (this.getMemory() != other.getMemory()) {
            return false;
        }
        if (this.controllerPort != other.controllerPort) {
            return false;
        }
        if (this.getControllerWebPort() != other.getControllerWebPort()) {
            return false;
        }
        if (this.getLogSize() != other.getLogSize()) {
            return false;
        }
        if (Double.compare(this.getCpu(), other.getCpu()) != 0) {
            return false;
        }
        final Object this$ethernetAddress = this.getEthernetAddress();
        final Object other$ethernetAddress = other.getEthernetAddress();
        return Objects.equals(this$ethernetAddress, other$ethernetAddress);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $controllerKey = this.getControllerKey();
        result = result * PRIME + ($controllerKey == null ? 43 : $controllerKey.hashCode());
        final Object $controllerIP = this.getControllerIP();
        result = result * PRIME + ($controllerIP == null ? 43 : $controllerIP.hashCode());
        final Object $clientName = this.getClientName();
        result = result * PRIME + ($clientName == null ? 43 : $clientName.hashCode());
        final Object $startIP = this.getStartIP();
        result = result * PRIME + ($startIP == null ? 43 : $startIP.hashCode());
        result = result * PRIME + this.getMemory();
        result = result * PRIME + this.controllerPort;
        result = result * PRIME + this.getControllerWebPort();
        result = result * PRIME + this.getLogSize();
        final long $cpu = Double.doubleToLongBits(this.getCpu());
        result = result * PRIME + (int) ($cpu >>> 32 ^ $cpu);
        final Object $ethernetAddress = this.getEthernetAddress();
        result = result * PRIME + ($ethernetAddress == null ? 43 : $ethernetAddress.hashCode());
        return result;
    }

    public String toString() {
        return "CloudConfiguration(controllerKey=" + this.getControllerKey() + ", controllerIP="
            + this.getControllerIP() + ", clientName=" + this.getClientName() + ", startIP=" + this
            .getStartIP() + ", memory=" + this.getMemory() + ", controllerPort=" + this
            .controllerPort + ", controllerWebPort=" + this.getControllerWebPort()
            + ", logSize=" + this.getLogSize() + ", cpu=" + this.getCpu() + ", ethernetAddress="
            + this.getEthernetAddress() + ")";
    }
}
