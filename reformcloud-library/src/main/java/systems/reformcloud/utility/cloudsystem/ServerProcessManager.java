/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.cloudsystem;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

public final class ServerProcessManager implements Serializable {

    /**
     * All registered proxies by their process uid
     */
    private Map<UUID, ServerInfo> serverProcessUIDMap = ReformCloudLibraryService
        .concurrentHashMap();

    /**
     * All registered proxies by their name
     */
    private Map<UUID, ProxyInfo> proxyProcessUIDMap = ReformCloudLibraryService.concurrentHashMap();

    /**
     * All registered servers by their process uid
     */
    private Map<String, ServerInfo> serverProcessNameMap = ReformCloudLibraryService
        .concurrentHashMap();

    /**
     * All registered servers by their process uid
     */
    private Map<String, ProxyInfo> proxyProcessNameMap = ReformCloudLibraryService
        .concurrentHashMap();

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
     * Get a specific proxy info
     *
     * @param uid The uid of the proxy process
     * @return The proxy info or {@code null} if the proxy isn't registered
     */
    public ProxyInfo getRegisteredProxyByUID(final UUID uid) {
        return this.proxyProcessUIDMap.getOrDefault(uid, null);
    }

    /**
     * Get a specific proxy info
     *
     * @param name The name of the process
     * @return The proxy info or {@code null} if the proxy isn't registered
     */
    public ProxyInfo getRegisteredProxyByName(final String name) {
        return this.proxyProcessNameMap.getOrDefault(name, null);
    }

    /**
     * Get a specific server info
     *
     * @param uid The uid of the server process
     * @return The server info or {@code null} if the server isn't registered
     */
    public ServerInfo getRegisteredServerByUID(final UUID uid) {
        return this.serverProcessUIDMap.getOrDefault(uid, null);
    }

    /**
     * Get a specific server info
     *
     * @param name The name of the server process
     * @return The server info or {@code null} if the server isn't registered
     */
    public ServerInfo getRegisteredServerByName(final String name) {
        return this.serverProcessNameMap.getOrDefault(name, null);
    }

    /**
     * Registers a specific proxy in the cloud system to reserve the name, uid and port
     *
     * @param uid The uid of the process which should be registered
     * @param name The name of the process which should be registered
     * @param proxyInfo The proxy info of the process
     * @return The current instance of this class
     */
    public ServerProcessManager registerProxyProcess(final UUID uid, final String name,
                                                     ProxyInfo proxyInfo) {
        this.proxyProcessUIDMap.put(uid, proxyInfo);
        this.proxyProcessNameMap.put(name, proxyInfo);
        return this;
    }

    /**
     * Registers a specific proxy in the cloud system to reserve the name, uid and port
     *
     * @param proxyInfo The proxy info congaing all needed information about the process
     * @return The current instance of this class
     */
    public ServerProcessManager registerProxyProcess(final ProxyInfo proxyInfo) {
        this.registerProxyProcess(
            proxyInfo.getCloudProcess().getProcessUID(), proxyInfo.getCloudProcess().getName(),
            proxyInfo
        );
        return this;
    }

    /**
     * Registers a specific server in the cloud system to reserve the name, uid and port
     *
     * @param uid The uid of the process which should be registered
     * @param name The name of the process which should be registered
     * @param serverInfo The server info of the process
     * @return The current instance of this class
     */
    public ServerProcessManager registerServerProcess(final UUID uid, final String name,
                                                      ServerInfo serverInfo) {
        this.serverProcessUIDMap.put(uid, serverInfo);
        this.serverProcessNameMap.put(name, serverInfo);
        return this;
    }

    /**
     * Unregisters a specific proxy in the cloud system
     *
     * @param uid The uid of the process which should be unregistered
     * @param name The name of the process which should be unregistered
     * @return The current instance of this class
     */
    public ServerProcessManager unregisterProxyProcess(final UUID uid, final String name) {
        this.proxyProcessUIDMap.remove(uid);
        this.proxyProcessNameMap.remove(name);
        return this;
    }

    /**
     * Unregisters a specific server in the cloud system
     *
     * @param uid The uid of the process which should be unregistered
     * @param name The name of the process which should be unregistered
     * @return The current instance of this class
     */
    public ServerProcessManager unregisterServerProcess(final UUID uid, final String name) {
        this.serverProcessUIDMap.remove(uid);
        this.serverProcessNameMap.remove(name);
        return this;
    }

    /**
     * Get all proxy infos by UID
     *
     * @return A list containing all registered proxy uid
     */
    public Set<UUID> getRegisteredProxyUIDProcesses() {
        return this.proxyProcessUIDMap.keySet();
    }

    /**
     * Get all proxy infos by name
     *
     * @return A list containing all registered proxy names
     */
    public Set<String> getRegisteredProxyNameProcesses() {
        return this.proxyProcessNameMap.keySet();
    }

    /**
     * Get all server infos by UID
     *
     * @return A list containing all registered server uid
     */
    public Set<UUID> getRegisteredServerUIDProcesses() {
        return this.serverProcessUIDMap.keySet();
    }

    /**
     * Get all server infos by name
     *
     * @return A list containing all registered server names
     */
    public Set<String> getRegisteredServerNameProcesses() {
        return this.serverProcessNameMap.keySet();
    }

    /**
     * Gets a proxy process is registered
     *
     * @param uid The uid of the process
     * @return If the proxy process is registered in the cloud system
     */
    public boolean isUIDProxyProcessRegistered(final UUID uid) {
        return this.proxyProcessUIDMap.containsKey(uid);
    }

    /**
     * Gets a proxy process is registered
     *
     * @param name The name of the proxy process
     * @return If the proxy process is registered in the cloud system
     */
    public boolean isNameProxyProcessRegistered(final String name) {
        return this.proxyProcessNameMap.containsKey(name);
    }

    /**
     * Gets a server process is registered
     *
     * @param uid The uid of the process
     * @return If the server process is registered in the cloud system
     */
    public boolean isUIDServerProcessRegistered(final UUID uid) {
        return this.serverProcessUIDMap.containsKey(uid);
    }

    /**
     * Gets a server process is registered
     *
     * @param name The name of the server process
     * @return If the server process is registered in the cloud system
     */
    public boolean isNameServerProcessRegistered(final String name) {
        return this.serverProcessNameMap.containsKey(name);
    }

    /**
     * Get the max memory used by the cloud system on the server site
     *
     * @return The max memory on the server site
     */
    public int getUsedServerMemory() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        this.serverProcessUIDMap.values().forEach(e -> atomicInteger.addAndGet(e.getMaxMemory()));
        return atomicInteger.get();
    }

    /**
     * Get the max memory used by the cloud system on the proxy site
     *
     * @return The max memory on the proxy site
     */
    public int getUsedProxyMemory() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        this.proxyProcessUIDMap.values().forEach(e -> atomicInteger.addAndGet(e.getMaxMemory()));
        return atomicInteger.get();
    }

    /**
     * Get all online servers of a specific group
     *
     * @param name The name of the group
     * @return A list containing all started servers of the given group by their name
     */
    public List<String> getOnlineServers(final String name) {
        List<String> list = new ArrayList<>();

        for (ServerInfo serverInfo : this.serverProcessUIDMap.values()) {
            if (serverInfo.getServerGroup().getName().equalsIgnoreCase(name)) {
                list.add(serverInfo.getServerGroup().getName());
            }
        }

        return list;
    }

    /**
     * Get all online proxies of a specific group
     *
     * @param name The name of the group
     * @return A list containing all started proxies of the given group by their name
     */
    public List<String> getOnlineProxies(final String name) {
        List<String> list = new ArrayList<>();

        for (ProxyInfo proxyInfo : this.proxyProcessUIDMap.values()) {
            if (proxyInfo.getProxyGroup().getName().equalsIgnoreCase(name)) {
                list.add(proxyInfo.getProxyGroup().getName());
            }
        }

        return list;
    }

    /**
     * Get all registered server processes
     *
     * @return A list containing all registered server processes
     */
    public List<ServerInfo> getAllRegisteredServerProcesses() {
        return new ArrayList<>(this.serverProcessUIDMap.values());
    }

    /**
     * Get all registered proxy processes
     *
     * @return A list containing all registered proxy processes
     */
    public List<ProxyInfo> getAllRegisteredProxyProcesses() {
        return new ArrayList<>(this.proxyProcessUIDMap.values());
    }

    /**
     * Gets all registered server processes by a specific group
     *
     * @param groupName The group name of the group for which the cloud should look for
     * @return A list containing all server infos of a specific group
     */
    public List<ServerInfo> getAllRegisteredServerGroupProcesses(final String groupName) {
        return this.serverProcessUIDMap.values().stream()
            .filter(e -> e.getServerGroup().getName().equals(groupName))
            .collect(Collectors.toList());
    }

    /**
     * Gets all registered proxy processes by a specific group
     *
     * @param groupName The group name of the group for which the cloud should look for
     * @return A list containing all proxy infos of a specific group
     */
    public List<ProxyInfo> getAllRegisteredProxyGroupProcesses(final String groupName) {
        return this.proxyProcessUIDMap.values().stream()
            .filter(e -> e.getProxyGroup().getName().equals(groupName))
            .collect(Collectors.toList());
    }

    /**
     * Updates a specific server info
     *
     * @param serverInfo The server info which should be updated
     */
    public void updateServerInfo(final ServerInfo serverInfo) {
        this.serverProcessNameMap.replace(serverInfo.getCloudProcess().getName(), serverInfo);
        this.serverProcessUIDMap.replace(serverInfo.getCloudProcess().getProcessUID(), serverInfo);
    }

    /**
     * Updates a specific proxy info
     *
     * @param proxyInfo The proxy info which should be updated
     */
    public void updateProxyInfo(final ProxyInfo proxyInfo) {
        this.proxyProcessNameMap.replace(proxyInfo.getCloudProcess().getName(), proxyInfo);
        this.proxyProcessUIDMap.replace(proxyInfo.getCloudProcess().getProcessUID(), proxyInfo);
    }
}
