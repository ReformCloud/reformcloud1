/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.info;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 02.02.2019
 */

public final class ClientInfo implements Serializable {
    private static final long serialVersionUID = 588638903114905632L;

    public int maxMemory, cpuCoresSystem;

    private boolean ready;

    private List<String> startedServers;
    private List<String> startedProxies;

    private int usedMemory;
    private double cpuUsage;
    private long systemMemoryUsage;
    private long systemMemoryMax;

    @java.beans.ConstructorProperties({"maxMemory", "cpuCoresSystem", "ready", "startedServers", "startedProxies", "usedMemory", "cpuUsage", "systemMemoryUsage", "systemMemoryMax"})
    public ClientInfo(int maxMemory, int cpuCoresSystem, boolean ready, List<String> startedServers, List<String> startedProxies, int usedMemory, double cpuUsage, long systemMemoryUsage, long systemMemoryMax) {
        this.maxMemory = maxMemory;
        this.cpuCoresSystem = cpuCoresSystem;
        this.ready = ready;
        this.startedServers = startedServers;
        this.startedProxies = startedProxies;
        this.usedMemory = usedMemory;
        this.cpuUsage = cpuUsage;
        this.systemMemoryUsage = systemMemoryUsage;
        this.systemMemoryMax = systemMemoryMax;
    }

    public int getMaxMemory() {
        return this.maxMemory;
    }

    public int getCpuCoresSystem() {
        return this.cpuCoresSystem;
    }

    public boolean isReady() {
        return this.ready;
    }

    public List<String> getStartedServers() {
        return this.startedServers;
    }

    public List<String> getStartedProxies() {
        return this.startedProxies;
    }

    public int getUsedMemory() {
        return this.usedMemory;
    }

    public double getCpuUsage() {
        return this.cpuUsage;
    }

    public long getSystemMemoryUsage() {
        return this.systemMemoryUsage;
    }

    public long getSystemMemoryMax() {
        return this.systemMemoryMax;
    }

    public void setMaxMemory(int maxMemory) {
        this.maxMemory = maxMemory;
    }

    public void setCpuCoresSystem(int cpuCoresSystem) {
        this.cpuCoresSystem = cpuCoresSystem;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void setUsedMemory(int usedMemory) {
        this.usedMemory = usedMemory;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public void setSystemMemoryUsage(long systemMemoryUsage) {
        this.systemMemoryUsage = systemMemoryUsage;
    }

    public void setSystemMemoryMax(long systemMemoryMax) {
        this.systemMemoryMax = systemMemoryMax;
    }
}
