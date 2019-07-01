/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.info;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.meta.CloudProcess;
import systems.reformcloud.meta.environment.RuntimeEnvironment;
import systems.reformcloud.meta.process.ProcessStartupInformation;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.utility.cloudsystem.EthernetAddress;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

public final class ProxyInfo implements Serializable {

    private static final long serialVersionUID = -3889580749596738985L;

    /**
     * The cloud process information of the process
     */
    private CloudProcess cloudProcess;

    /**
     * The proxy group of the process
     */
    private ProxyGroup proxyGroup;

    /**
     * The process startup information
     */
    private ProcessStartupInformation processStartupInformation;

    /**
     * The current runtime operating system (set by the client)
     */
    private final RuntimeEnvironment runtimeEnvironment =
        ReformCloudLibraryService.runtimeEnvironment();

    /**
     * The host of the current proxy
     */
    private String host;

    /**
     * The port which is selected
     */
    private int port;

    /**
     * The online count of the process
     */
    private int online;

    /**
     * The max memory of the process
     */
    private int maxMemory;

    /**
     * The status if the server is full this is {@code true}
     */
    private boolean full;

    /**
     * A list of uuid's containing all online players uuid's
     */
    private List<UUID> onlinePlayers;

    @ConstructorProperties({"cloudProcess", "proxyGroup", "processStartupInformation",
        "host", "port", "online", "maxMemory", "full", "onlinePlayers"})
    public ProxyInfo(CloudProcess cloudProcess,
                     ProxyGroup proxyGroup,
                     ProcessStartupInformation processStartupInformation,
                     String host,
                     int port,
                     int online,
                     int maxMemory,
                     boolean full,
                     List<UUID> onlinePlayers) {
        this.cloudProcess = cloudProcess;
        this.proxyGroup = proxyGroup;
        this.processStartupInformation = processStartupInformation;
        this.host = host;
        this.port = port;
        this.online = online;
        this.maxMemory = maxMemory;
        this.full = full;
        this.onlinePlayers = onlinePlayers;
    }

    public CloudProcess getCloudProcess() {
        return this.cloudProcess;
    }

    public ProxyGroup getProxyGroup() {
        return this.proxyGroup;
    }

    public RuntimeEnvironment getRuntimeEnvironment() {
        return runtimeEnvironment;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public int getOnline() {
        return this.online;
    }

    public int getMaxMemory() {
        return this.maxMemory;
    }

    public ProcessStartupInformation getProcessStartupInformation() {
        return processStartupInformation;
    }

    public EthernetAddress toEtherNetAddress() {
        return new EthernetAddress(host, port);
    }

    public boolean isFull() {
        return this.full;
    }

    public List<UUID> getOnlinePlayers() {
        return this.onlinePlayers;
    }

    public void setCloudProcess(CloudProcess cloudProcess) {
        this.cloudProcess = cloudProcess;
    }

    public void setProxyGroup(ProxyGroup proxyGroup) {
        this.proxyGroup = proxyGroup;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public void setMaxMemory(int maxMemory) {
        this.maxMemory = maxMemory;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public void setOnlinePlayers(List<UUID> onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }
}
