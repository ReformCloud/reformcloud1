/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.info;

import systems.reformcloud.meta.CloudProcess;
import systems.reformcloud.meta.enums.ServerState;
import systems.reformcloud.meta.server.ServerGroup;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

public final class ServerInfo implements Serializable {

    private static final long serialVersionUID = 8057730391607929124L;

    private CloudProcess cloudProcess;

    private ServerGroup serverGroup;

    private ServerState serverState;

    private String host;

    private String motd;

    private int port;

    private int online;

    private int maxMemory;

    private boolean full;

    private List<UUID> onlinePlayers;

    @java.beans.ConstructorProperties({"cloudProcess", "serverGroup", "serverState", "host", "motd",
        "port", "online", "maxMemory", "full", "onlinePlayers"})
    public ServerInfo(CloudProcess cloudProcess, ServerGroup serverGroup, ServerState serverState,
        String host, String motd, int port, int online, int maxMemory, boolean full,
        List<UUID> onlinePlayers) {
        this.cloudProcess = cloudProcess;
        this.serverGroup = serverGroup;
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
