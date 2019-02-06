/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.cloudsystem;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

public class ServerProcessManager {
    private Map<UUID, ServerInfo> serverProcessUIDMap = ReformCloudLibraryService.concurrentHashMap();
    private Map<UUID, ProxyInfo> proxyProcessUIDMap = ReformCloudLibraryService.concurrentHashMap();

    private Map<String, ServerInfo> serverProcessNameMap = ReformCloudLibraryService.concurrentHashMap();
    private Map<String, ProxyInfo> proxyProcessNameMap = ReformCloudLibraryService.concurrentHashMap();

    private List<Integer> ports = new ArrayList<>();

    /**
     * Clears all registered Processes
     *
     * @deprecated
     */
    @Deprecated
    public void clearProcesses() {
        this.proxyProcessNameMap.clear();
        this.proxyProcessUIDMap.clear();
        this.serverProcessNameMap.clear();
        this.serverProcessUIDMap.clear();
    }

    /**
     * Get a specific ProxyProcess by {@link UUID}
     *
     * @param uid
     * @return the {@link ProxyInfo} or null if the processUID isn't registered
     */
    public ProxyInfo getRegisteredProxyByUID(final UUID uid) {
        return this.proxyProcessUIDMap.getOrDefault(uid, null);
    }

    /**
     * Get a specific ProxyProcess by {@link String}
     *
     * @param name
     * @return the {@link ProxyInfo} or null if the processName isn't registered
     */
    public ProxyInfo getRegisteredProxyByName(final String name) {
        return this.proxyProcessNameMap.getOrDefault(name, null);
    }

    /**
     * Get a specific ServerProcess by {@link UUID}
     *
     * @param uid
     * @return the {@link ServerInfo} or null if the processUID isn't registered
     */
    public ServerInfo getRegisteredServerByUID(final UUID uid) {
        return this.serverProcessUIDMap.getOrDefault(uid, null);
    }

    /**
     * Get a specific ServerProcess by {@link String}
     *
     * @param name
     * @return the {@link ServerInfo} or null if the processName isn't registered
     */
    public ServerInfo getRegisteredServerByName(final String name) {
        return this.serverProcessNameMap.getOrDefault(name, null);
    }

    /**
     * Registers a specific ProxyProcess, by UID {@link UUID},
     * name {@link String},
     * proxyInfo {@link ProxyInfo},
     * port {@link Integer}
     *
     * @param uid
     * @param name
     * @param proxyInfo
     * @param port
     * @return this;
     */
    public ServerProcessManager registerProxyProcess(final UUID uid, final String name, ProxyInfo proxyInfo, final int port) {
        this.proxyProcessUIDMap.put(uid, proxyInfo);
        this.proxyProcessNameMap.put(name, proxyInfo);
        this.ports.add(port);
        return this;
    }

    /**
     * Registers a specific ServerProcess, by UID {@link UUID},
     * name {@link String},
     * serverInfo {@link ServerInfo},
     * port {@link Integer}
     *
     * @param uid
     * @param name
     * @param serverInfo
     * @param port
     * @return this;
     */
    public ServerProcessManager registerServerProcess(final UUID uid, final String name, ServerInfo serverInfo, final int port) {
        this.serverProcessUIDMap.put(uid, serverInfo);
        this.serverProcessNameMap.put(name, serverInfo);
        this.ports.add(port);
        return this;
    }

    /**
     * Unregisters a specific ProxyProcess, by UID {@link UUID},
     * name {@link String},
     * port {@link Integer}
     *
     * @param uid
     * @param name
     * @param port
     * @return this
     */
    public ServerProcessManager unregisterProxyProcess(final UUID uid, final String name, final int port) {
        this.proxyProcessUIDMap.remove(uid);
        this.proxyProcessNameMap.remove(name);
        this.ports.remove(Integer.valueOf(port));
        return this;
    }

    /**
     * Unregisters a specific ProxyProcess, by UID {@link UUID},
     * name {@link String},
     * port {@link Integer}
     *
     * @param uid
     * @param name
     * @param port
     * @return this
     */
    public ServerProcessManager unregisterServerProcess(final UUID uid, final String name, final int port) {
        this.serverProcessUIDMap.remove(uid);
        this.serverProcessNameMap.remove(name);
        this.ports.remove(Integer.valueOf(port));
        return this;
    }

    /**
     * Get all registeredProxyProcesses by UID
     *
     * @return {@link Set<UUID>} with UID {@link UUID} of all registered ProxyProcesses
     */
    public Set<UUID> getRegisteredProxyUIDProcesses() {
        return this.proxyProcessUIDMap.keySet();
    }

    /**
     * Get all registeredProxyProcesses by name
     *
     * @return {@link Set<String>} with name {@link String} of all registered ProxyProcesses
     */
    public Set<String> getRegisteredProxyNameProcesses() {
        return this.proxyProcessNameMap.keySet();
    }

    /**
     * Get all registeredServerProcesses by UID
     *
     * @return {@link Set<UUID>} with UID {@link UUID} of all registered ServerProcesses
     */
    public Set<UUID> getRegisteredServerUIDProcesses() {
        return this.serverProcessUIDMap.keySet();
    }

    /**
     * Get all registeredServerProcesses by name
     *
     * @return {@link Set<String>} with names {@link String} of all registered ServerProcesses
     */
    public Set<String> getRegisteredServerNameProcesses() {
        return this.serverProcessNameMap.keySet();
    }

    /**
     * Get if a proxyProcess is registered by UID {@link UUID}
     *
     * @param uid
     * @return if the proxyProcess is registered
     */
    public boolean isUIDProxyProcessRegistered(final UUID uid) {
        return this.proxyProcessUIDMap.containsKey(uid);
    }

    /**
     * Get if a proxyProcess is registered by name {@link String}
     *
     * @param name
     * @return if the proxyProcess is registered
     */
    public boolean isNameProxyProcessRegistered(final String name) {
        return this.proxyProcessNameMap.containsKey(name);
    }

    /**
     * Get if a serverProcess is registered by UID {@link UUID}
     *
     * @param uid
     * @return if the serverProcess is registered
     */
    public boolean isUIDServerProcessRegistered(final UUID uid) {
        return this.serverProcessUIDMap.containsKey(uid);
    }

    /**
     * Get if a serverProcess is registered by name {@link String}
     *
     * @param name
     * @return if the serverProcess is registered
     */
    public boolean isNameServerProcessRegistered(final String name) {
        return this.serverProcessNameMap.containsKey(name);
    }

    /**
     * Get maximal memory of all registered serverProcesses
     *
     * @return maximal memory of all registered serverProcesses as {@link Integer}
     */
    public int getUsedServerMemory() {
        int memory = 0;
        for (ServerInfo serverInfo : this.serverProcessUIDMap.values())
            memory = memory + serverInfo.getMaxMemory();

        return memory;
    }

    /**
     * Get maximal memory of all registered proxyProcesses
     *
     * @return maximal memory of all registered proxyProcesses as {@link Integer}
     */
    public int getUsedProxyMemory() {
        int memory = 0;
        for (ProxyInfo proxyInfo : this.proxyProcessUIDMap.values())
            memory = memory + proxyInfo.getMaxMemory();

        return memory;
    }

    /**
     * Get all online Servers
     *
     * @param name
     * @return {@link List<String>} of all registered serverProcesses
     */
    public List<String> getOnlineServers(final String name) {
        List<String> list = new ArrayList<>();

        for (ServerInfo serverInfo : this.serverProcessUIDMap.values())
            if (serverInfo.getServerGroup().getName().equalsIgnoreCase(name))
                list.add(serverInfo.getServerGroup().getName());

        return list;
    }

    /**
     * Get all online Proxies
     *
     * @param name
     * @return {@link List<String>} of all registered proxyProcesses
     */
    public List<String> getOnlineProxies(final String name) {
        List<String> list = new ArrayList<>();

        for (ProxyInfo proxyInfo : this.proxyProcessUIDMap.values())
            if (proxyInfo.getProxyGroup().getName().equalsIgnoreCase(name))
                list.add(proxyInfo.getProxyGroup().getName());

        return list;
    }

    /**
     * Gets the next LobbyServer for the given permissions
     *
     * @param permissions
     * @return
     */
    public ServerInfo nextFreeLobby(final Collection<String> permissions) {
        for (ServerInfo serverInfo : this.serverProcessUIDMap.values()) {
            if (serverInfo.getServerGroup().getServerModeType().equals(ServerModeType.STATIC)
                    || serverInfo.getServerGroup().getServerModeType().equals(ServerModeType.DYNAMIC)) {
                continue;
            }

            if (serverInfo.getServerGroup().getJoin_permission() == null && serverInfo.getOnlinePlayers().size() < serverInfo.getServerGroup().getMaxPlayers())
                return serverInfo;
            else if (permissions.contains(serverInfo.getServerGroup().getJoin_permission()) && serverInfo.getOnlinePlayers().size() < serverInfo.getServerGroup().getMaxPlayers())
                return serverInfo;
        }

        return null;
    }

    /**
     * Get if the given port is already registered
     *
     * @param port
     * @return if the given port is already registered
     */
    public boolean isPortRegistered(final int port) {
        return this.ports.contains(port);
    }

    public String nextFreeServerID(final String groupName) {
        int id = 1;
        List<Integer> ids = new ArrayList<>();
        this.serverProcessUIDMap.values().stream().filter(e -> e.getServerGroup().getName().equals(groupName)).forEach(e -> ids.add(e.getCloudProcess().getProcessID()));
        while (ids.contains(id)) {
            id++;
        }

        return Integer.toString(id);
    }

    public String nextFreeProxyID(final String groupName) {
        int id = 1;
        List<Integer> ids = new ArrayList<>();
        this.proxyProcessUIDMap.values().stream().filter(e -> e.getProxyGroup().getName().equals(groupName)).forEach(e -> ids.add(e.getCloudProcess().getProcessID()));
        while (ids.contains(id)) {
            id++;
        }

        return Integer.toString(id);
    }

    /**
     * Get next free port
     *
     * @param startPort
     * @return next free port, starting at the given {@param startPort}
     */
    public int nextFreePort(int startPort) {
        while (this.ports.contains(startPort)) {
            startPort = startPort + 1;
        }
        return startPort;
    }

    /**
     * Get all registered ServerInfo
     *
     * @return {@link ArrayList<ServerInfo>} of all serverProcesses
     */
    public List<ServerInfo> getAllRegisteredServerProcesses() {
        return new ArrayList<>(this.serverProcessUIDMap.values());
    }

    /**
     * Get all registered ProxyInfo
     *
     * @return {@link ArrayList<ProxyInfo>} of all proxyProcesses
     */
    public List<ProxyInfo> getAllRegisteredProxyProcesses() {
        return new ArrayList<>(this.proxyProcessUIDMap.values());
    }

    /**
     * Gets all registered ServerProcesses by GroupName
     *
     * @param groupName
     * @return all registered ServerProcesses by GroupName
     */
    public List<ServerInfo> getAllRegisteredServerGroupProcesses(final String groupName) {
        return this.serverProcessUIDMap.values().stream().filter(e -> e.getServerGroup().getName().equals(groupName)).collect(Collectors.toList());
    }

    /**
     * Gets all registered ProxyProcesses by GroupName
     *
     * @param groupName
     * @return all registered ProxyProcesses by GroupName
     */
    public List<ProxyInfo> getAllRegisteredProxyGroupProcesses(final String groupName) {
        return this.proxyProcessUIDMap.values().stream().filter(e -> e.getProxyGroup().getName().equals(groupName)).collect(Collectors.toList());
    }

    /**
     * Updates a specific serverInfo
     *
     * @param serverInfo
     */
    public void updateServerInfo(final ServerInfo serverInfo) {
        this.serverProcessNameMap.remove(serverInfo.getCloudProcess().getName());
        this.serverProcessUIDMap.remove(serverInfo.getCloudProcess().getProcessUID());

        this.serverProcessNameMap.put(serverInfo.getCloudProcess().getName(), serverInfo);
        this.serverProcessUIDMap.put(serverInfo.getCloudProcess().getProcessUID(), serverInfo);
    }

    /**
     * Updates a specific proxyInfo
     *
     * @param proxyInfo
     */
    public void updateProxyInfo(final ProxyInfo proxyInfo) {
        this.proxyProcessNameMap.remove(proxyInfo.getCloudProcess().getName());
        this.proxyProcessUIDMap.remove(proxyInfo.getCloudProcess().getProcessUID());

        this.proxyProcessNameMap.put(proxyInfo.getCloudProcess().getName(), proxyInfo);
        this.proxyProcessUIDMap.put(proxyInfo.getCloudProcess().getProcessUID(), proxyInfo);
    }
}
