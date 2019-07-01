/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.info;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.meta.CloudProcess;
import systems.reformcloud.meta.enums.ServerState;
import systems.reformcloud.meta.environment.RuntimeEnvironment;
import systems.reformcloud.meta.process.ProcessStartupInformation;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.utility.cloudsystem.EthernetAddress;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

public final class ServerInfo implements Serializable {

    private static final long serialVersionUID = 8057730391607929124L;

    /**
     * The cloud process information of the process
     */
    private CloudProcess cloudProcess;

    /**
     * The server group of the process
     */
    private ServerGroup serverGroup;

    /**
     * The process startup information
     */
    private ProcessStartupInformation processStartupInformation;

    /**
     * The server state of the process
     */
    private ServerState serverState;

    /**
     * The current runtime operating system (set by the client)
     */
    private final RuntimeEnvironment runtimeEnvironment =
        ReformCloudLibraryService.runtimeEnvironment();

    /**
     * The host of the current server
     */
    private String host;

    /**
     * The motd of the current server
     */
    private String motd;

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

    @ConstructorProperties({"cloudProcess", "serverGroup", "processStartupInformation", "serverState",
        "host", "motd", "port", "online", "maxMemory", "full", "onlinePlayers"})
    public ServerInfo(CloudProcess cloudProcess,
                      ServerGroup serverGroup,
                      ProcessStartupInformation processStartupInformation,
                      ServerState serverState,
                      String host,
                      String motd,
                      int port,
                      int online,
                      int maxMemory,
                      boolean full,
                      List<UUID> onlinePlayers) {
        this.cloudProcess = cloudProcess;
        this.serverGroup = serverGroup;
        this.processStartupInformation = processStartupInformation;
        this.serverState = serverState;
        this.host = host;
        this.motd = motd;
        this.port = port;
        this.online = online;
        this.maxMemory = maxMemory;
        this.full = full;
        this.onlinePlayers = onlinePlayers;
    }

    public CloudProcess getCloudProcess() {
        return this.cloudProcess;
    }

    public ServerGroup getServerGroup() {
        return this.serverGroup;
    }

    public ServerState getServerState() {
        return this.serverState;
    }

    public RuntimeEnvironment getRuntimeEnvironment() {
        return runtimeEnvironment;
    }

    public String getHost() {
        return this.host;
    }

    public String getMotd() {
        return this.motd;
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

    public boolean isFull() {
        return this.full;
    }

    public List<UUID> getOnlinePlayers() {
        return this.onlinePlayers;
    }

    public ProcessStartupInformation getProcessStartupInformation() {
        return processStartupInformation;
    }

    public EthernetAddress toEtherNetAddress() {
        return new EthernetAddress(host, port);
    }

    public void setCloudProcess(CloudProcess cloudProcess) {
        this.cloudProcess = cloudProcess;
    }

    public void setServerGroup(ServerGroup serverGroup) {
        this.serverGroup = serverGroup;
    }

    public void setServerState(ServerState serverState) {
        this.serverState = serverState;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setMotd(String motd) {
        this.motd = motd;
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
