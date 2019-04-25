/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.configuration;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.utility.ExitUtil;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.checkable.Checkable;
import systems.reformcloud.utility.cloudsystem.EthernetAddress;
import systems.reformcloud.utility.files.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author _Klaro | Pasqual K. / created on 24.10.2018
 */

public final class CloudConfiguration implements Serializable {
    private String controllerKey, controllerIP, clientName, startIP;
    private int memory, controllerPort, controllerWebPort, logSize;
    private double cpu;

    private EthernetAddress ethernetAddress;

    /**
     * Creates or/and loads the Client Configuration
     */
    public CloudConfiguration(final boolean reload) {
        if (!Files.exists(Paths.get("configuration.properties")) && !Files.exists(Paths.get("ControllerKEY"))) {
            ReformCloudClient.getInstance().getLoggerProvider().serve("Please copy the \"ControllerKEY\" file in the root directory of the client");
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
        })
            dir.mkdirs();
    }

    /**
     * Creates default Configuration or
     * returns if the Configuration already exists
     */
    private boolean defaultInit() {
        if (Files.exists(Paths.get("configuration.properties")))
            return false;

        LoggerProvider loggerProvider = ReformCloudClient.getInstance().getLoggerProvider();

        loggerProvider.info("Please provide the internal ReformCloudClient ip");
        String ip = this.readString(loggerProvider, s -> s.split("\\.").length == 4);
        loggerProvider.info("Please provide the Controller IP");
        String controllerIP = this.readString(loggerProvider, s -> s.split("\\.").length == 4);

        ReformCloudClient.getInstance().getLoggerProvider().info("Please provide the name of this Client. (Recommended: Client-01)");
        String clientID = this.readString(loggerProvider, s -> true);

        Properties properties = new Properties();

        properties.setProperty("controller.ip", controllerIP);
        properties.setProperty("controller.port", 5000 + StringUtil.EMPTY);
        properties.setProperty("controller.web-port", 4790 + StringUtil.EMPTY);

        properties.setProperty("general.client-name", clientID);
        properties.setProperty("general.start-host", ip);
        properties.setProperty("general.memory", 1024 + StringUtil.EMPTY);
        properties.setProperty("general.maxcpuusage", 90.00 + StringUtil.EMPTY);
        properties.setProperty("general.max-log-size", Integer.toString(46));

        try (OutputStream outputStream = Files.newOutputStream(Paths.get("configuration.properties"))) {
            properties.store(outputStream, "ReformCloud default Configuration");
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not store configuration.properties", ex);
            return false;
        }

        return true;
    }

    public void clearServerTemp() {
        final File dir = new File("reformcloud/temp/servers");

        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    FileUtils.deleteFullDirectory(file.toPath());
                }
            }
        }
    }

    public void clearProxyTemp() {
        final File dir = new File("reformcloud/temp/proxies");

        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
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
        try (InputStreamReader inputStreamReader = new InputStreamReader(Files.newInputStream(Paths.get("configuration.properties")))) {
            properties.load(inputStreamReader);
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not load configuration.properties", ex);
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

        this.controllerKey = FileUtils.readFileAsString(new File("reformcloud/files/ControllerKEY"));
    }

    private String readString(final LoggerProvider loggerProvider, Checkable<String> checkable) {
        String readLine = loggerProvider.readLine();
        while (readLine == null || !checkable.isChecked(readLine) || readLine.trim().isEmpty()) {
            loggerProvider.info("Input invalid, please try again");
            readLine = loggerProvider.readLine();
        }

        return readLine;
    }

    private Integer readInt(final LoggerProvider loggerProvider, Checkable<Integer> checkable) {
        String readLine = loggerProvider.readLine();
        while (readLine == null || readLine.trim().isEmpty() || !ReformCloudLibraryService.checkIsInteger(readLine) || !checkable.isChecked(Integer.parseInt(readLine))) {
            loggerProvider.info("Input invalid, please try again");
            readLine = loggerProvider.readLine();
        }

        return Integer.parseInt(readLine);
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

    public int getControllerPort() {
        return this.controllerPort;
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

    public void setControllerKey(String controllerKey) {
        this.controllerKey = controllerKey;
    }

    public void setControllerIP(String controllerIP) {
        this.controllerIP = controllerIP;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setStartIP(String startIP) {
        this.startIP = startIP;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public void setControllerPort(int controllerPort) {
        this.controllerPort = controllerPort;
    }

    public void setControllerWebPort(int controllerWebPort) {
        this.controllerWebPort = controllerWebPort;
    }

    public void setLogSize(int logSize) {
        this.logSize = logSize;
    }

    public void setCpu(double cpu) {
        this.cpu = cpu;
    }

    public void setEthernetAddress(EthernetAddress ethernetAddress) {
        this.ethernetAddress = ethernetAddress;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CloudConfiguration)) return false;
        final CloudConfiguration other = (CloudConfiguration) o;
        final Object this$controllerKey = this.getControllerKey();
        final Object other$controllerKey = other.getControllerKey();
        if (this$controllerKey == null ? other$controllerKey != null : !this$controllerKey.equals(other$controllerKey))
            return false;
        final Object this$controllerIP = this.getControllerIP();
        final Object other$controllerIP = other.getControllerIP();
        if (this$controllerIP == null ? other$controllerIP != null : !this$controllerIP.equals(other$controllerIP))
            return false;
        final Object this$clientName = this.getClientName();
        final Object other$clientName = other.getClientName();
        if (this$clientName == null ? other$clientName != null : !this$clientName.equals(other$clientName))
            return false;
        final Object this$startIP = this.getStartIP();
        final Object other$startIP = other.getStartIP();
        if (this$startIP == null ? other$startIP != null : !this$startIP.equals(other$startIP)) return false;
        if (this.getMemory() != other.getMemory()) return false;
        if (this.getControllerPort() != other.getControllerPort()) return false;
        if (this.getControllerWebPort() != other.getControllerWebPort()) return false;
        if (this.getLogSize() != other.getLogSize()) return false;
        if (Double.compare(this.getCpu(), other.getCpu()) != 0) return false;
        final Object this$ethernetAddress = this.getEthernetAddress();
        final Object other$ethernetAddress = other.getEthernetAddress();
        if (this$ethernetAddress == null ? other$ethernetAddress != null : !this$ethernetAddress.equals(other$ethernetAddress))
            return false;
        return true;
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
        result = result * PRIME + this.getControllerPort();
        result = result * PRIME + this.getControllerWebPort();
        result = result * PRIME + this.getLogSize();
        final long $cpu = Double.doubleToLongBits(this.getCpu());
        result = result * PRIME + (int) ($cpu >>> 32 ^ $cpu);
        final Object $ethernetAddress = this.getEthernetAddress();
        result = result * PRIME + ($ethernetAddress == null ? 43 : $ethernetAddress.hashCode());
        return result;
    }

    public String toString() {
        return "CloudConfiguration(controllerKey=" + this.getControllerKey() + ", controllerIP=" + this.getControllerIP() + ", clientName=" + this.getClientName() + ", startIP=" + this.getStartIP() + ", memory=" + this.getMemory() + ", controllerPort=" + this.getControllerPort() + ", controllerWebPort=" + this.getControllerWebPort() + ", logSize=" + this.getLogSize() + ", cpu=" + this.getCpu() + ", ethernetAddress=" + this.getEthernetAddress() + ")";
    }
}
