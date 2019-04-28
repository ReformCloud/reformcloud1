/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.info;

import systems.reformcloud.meta.CloudProcess;
import systems.reformcloud.meta.proxy.ProxyGroup;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

public final class ProxyInfo implements Serializable {
    private static final long serialVersionUID = -3889580749596738985L;

    private CloudProcess cloudProcess;
    private ProxyGroup proxyGroup;

    private String host;
    private int port, online, maxMemory;

    private boolean full;
    private List<UUID> onlinePlayers;

    @java.beans.ConstructorProperties({"cloudProcess", "proxyGroup", "host", "port", "online", "maxMemory", "full", "onlinePlayers"})
    public ProxyInfo(CloudProcess cloudProcess, ProxyGroup proxyGroup, String host, int port, int online, int maxMemory, boolean full, List<UUID> onlinePlayers) {
        this.cloudProcess = cloudProcess;
        this.proxyGroup = proxyGroup;
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
