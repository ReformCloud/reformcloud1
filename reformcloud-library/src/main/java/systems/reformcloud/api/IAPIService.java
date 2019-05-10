/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api;

import systems.reformcloud.api.save.ISaveAPIService;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.Template;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.dev.DevProcess;
import systems.reformcloud.meta.enums.ProxyModeType;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.proxy.versions.ProxyVersions;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.server.versions.SpigotVersions;
import systems.reformcloud.meta.web.WebUser;
import systems.reformcloud.network.NettyHandler;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.network.packet.PacketFuture;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.player.implementations.OnlinePlayer;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author _Klaro | Pasqual K. / created on 17.02.2019
 */

public interface IAPIService {
    /**
     * The atomic reference to get the default instance of the api service
     */
    AtomicReference<IAPIService> instance = new AtomicReference<>();

    /**
     * Starts a game server
     *
     * @param serverGroupName The name of the ServerGroup you want
     *                        to start a server of
     */
    void startGameServer(String serverGroupName);

    /**
     * Starts a game server
     *
     * @param serverGroupName The name of the ServerGroup you want
     *                        to start a server of
     * @param preConfig       The preConfig which will be saved while
     *                        starting up the process
     */
    void startGameServer(String serverGroupName, Configuration preConfig);

    /**
     * Starts a game server
     *
     * @param serverGroup The specific ServerGroup you want to
     *                    start a server of
     */
    void startGameServer(ServerGroup serverGroup);

    /**
     * Starts a game server
     *
     * @param serverGroup The specific ServerGroup you want to
     *                    start a server of
     * @param preConfig   The preConfig which will be saved while
     *                    starting up the process
     */
    void startGameServer(ServerGroup serverGroup, Configuration preConfig);

    /**
     * Starts a game server
     *
     * @param serverGroup The specific ServerGroup you want to
     *                    start a server of
     * @param preConfig   The preConfig which will be saved while
     *                    starting up the process
     * @param template    The specific template the server should
     *                    start with
     */
    void startGameServer(ServerGroup serverGroup, Configuration preConfig, String template);

    /**
     * Starts a proxy
     *
     * @param proxyGroupName The name of the ProxyGroup you want
     *                       to start a proxy of
     */
    void startProxy(String proxyGroupName);

    /**
     * Starts a proxy
     *
     * @param proxyGroupName The name of the ProxyGroup you want
     *                       to start a proxy of
     * @param preConfig      The preConfig which will be saved while
     *                       starting up the process
     */
    void startProxy(String proxyGroupName, Configuration preConfig);

    /**
     * Starts a proxy
     *
     * @param proxyGroup The exact ProxyGroup you want
     *                   to start a process of
     */
    void startProxy(ProxyGroup proxyGroup);

    /**
     * Starts a proxy
     *
     * @param proxyGroup The exact ProxyGroup you want
     *                   to start a process of
     * @param preConfig  The preConfig which will be saved while
     *                   starting up the process
     */
    void startProxy(ProxyGroup proxyGroup, Configuration preConfig);

    /**
     * Starts a proxy
     *
     * @param proxyGroup The exact ProxyGroup you want
     *                   to start a process of
     * @param preConfig  The preConfig which will be saved while
     *                   starting up the process
     * @param template   The exact template that should be loaded
     *                   while starting up the process
     */
    void startProxy(ProxyGroup proxyGroup, Configuration preConfig, String template);

    /**
     * Stops a specific proxy
     *
     * @param name The name of the proxy process
     * @return If the process is registered or not
     */
    boolean stopProxy(String name);

    /**
     * Stops a specific proxy
     *
     * @param proxyInfo     The proxy info of the proxy process
     * @return If the process is registered or not
     */
    boolean stopProxy(ProxyInfo proxyInfo);

    /**
     * Stops a specific server
     *
     * @param name      The name of the server process
     * @return If the server is registered
     */
    boolean stopServer(String name);

    /**
     * Stops a specific server
     *
     * @param serverInfo        The server info of the process
     * @return If the process is registered
     */
    boolean stopServer(ServerInfo serverInfo);

    /**
     * Creates a new server group
     *
     * @param name                  The name of the new server group
     * @param serverModeType        The server mode type of the new group
     * @param clients               The clients which should be used for the group
     * @param spigotVersions        The spigot version which should be used
     */
    void createServerGroup(String name, ServerModeType serverModeType, Collection<String> clients, SpigotVersions spigotVersions);

    /**
     * Creates a new server group
     *
     * @param serverGroup       The server group object of the new group
     */
    void createServerGroup(ServerGroup serverGroup);

    /**
     * Creates a new server group
     *
     * @param name      The name of the new server group
     */
    void createServerGroup(String name);

    /**
     * Creates a new server group
     *
     * @param name                  The name of the new server group
     * @param serverModeType        The server mode which should be used
     * @param templates             The list of the templates for the new group
     */
    void createServerGroup(String name, ServerModeType serverModeType, Collection<Template> templates);

    /**
     * Creates a new server group
     *
     * @param name                  The name of the new server group
     * @param serverModeType        The server mode type which should be used
     * @param clients               The clients of the new server group
     * @param templates             The templates of the new server group
     * @param spigotVersions        The spigot version which should be used
     */
    void createServerGroup(String name, ServerModeType serverModeType, Collection<String> clients, Collection<Template> templates, SpigotVersions spigotVersions);

    /**
     * Creates a new proxy group
     *
     * @param name              The name of the new proxy group
     * @param proxyModeType     The proxy mode type which should be used
     * @param clients           The clients of the new proxy group
     * @param proxyVersions     The proxy version which should be used
     */
    void createProxyGroup(String name, ProxyModeType proxyModeType, Collection<String> clients, ProxyVersions proxyVersions);

    /**
     * Creates a new proxy group
     *
     * @param proxyGroup        The proxy group object of the new group
     */
    void createProxyGroup(ProxyGroup proxyGroup);

    /**
     * Creates a new proxy group
     *
     * @param name      The name of the new proxy group
     */
    void createProxyGroup(String name);

    /**
     * Creates a new proxy group
     *
     * @param name              The name of the new proxy group
     * @param proxyModeType     The proxy mode type which should be used
     * @param templates         The templates of the new proxy group
     */
    void createProxyGroup(String name, ProxyModeType proxyModeType, Collection<Template> templates);

    /**
     * Creates a new proxy group
     *
     * @param name              The name of the new proxy group
     * @param proxyModeType     The proxy mode type which should be used
     * @param clients           The clients of the proxy group
     * @param templates         The templates of the proxy group
     * @param proxyVersions     The proxy version which should be used
     */
    void createProxyGroup(String name, ProxyModeType proxyModeType, Collection<String> clients, Collection<Template> templates, ProxyVersions proxyVersions);

    /**
     * Creates a new client
     *
     * @param name      The name of the new client
     * @param host      The host of the new client
     */
    void createClient(String name, String host);

    /**
     * Creates a new local client
     *
     * @param name      The name of the new client
     */
    void createClient(String name);

    /**
     * Creates a new client
     *
     * @param client        The client object
     */
    void createClient(Client client);

    /**
     * Updates a specific server info
     *
     * @param serverInfo        The server info which should be updated
     */
    void updateServerInfo(ServerInfo serverInfo);

    /**
     * Updates a specific proxy info
     *
     * @param proxyInfo         The proxy info which should be updated
     */
    void updateProxyInfo(ProxyInfo proxyInfo);

    /**
     * Updates a specific server group
     *
     * @param serverGroup       The server group which should be updated
     */
    void updateServerGroup(ServerGroup serverGroup);

    /**
     * Updates a specific proxy group
     *
     * @param proxyGroup        The proxy group which should be updated
     */
    void updateProxyGroup(ProxyGroup proxyGroup);

    /**
     * Creates a new web user
     *
     * @param name      The name of the new web user
     */
    void createWebUser(String name);

    /**
     * Creates a new web user
     *
     * @param name          The name of the new web user
     * @param password      The password of the new web user
     */
    void createWebUser(String name, String password);

    /**
     * Creates a new web user
     *
     * @param name              The name of the new web user
     * @param password          The password of the new web user
     * @param permissions       The permissions of the new web user
     */
    void createWebUser(String name, String password, Map<String, Boolean> permissions);

    /**
     * Creates a new web user
     *
     * @param webUser       The web user object
     */
    void createWebUser(WebUser webUser);

    /**
     * Dispatches a console command to the controller
     *
     * @param commandLine The command which should be executed
     */
    void dispatchConsoleCommand(String commandLine);

    /**
     * Dispatches a console command to the controller
     *
     * @param commandLine The command which should be executed
     */
    void dispatchConsoleCommand(CharSequence commandLine);

    /**
     * Dispatches a console command to the controller
     *
     * @param commandLine The command which should be executed
     * @return The first output line of the command in the controller console
     */
    String dispatchConsoleCommandAndGetResult(String commandLine);

    /**
     * Dispatches a console command to the controller
     *
     * @param commandLine The command which should be executed
     * @return The first output line of the command in the controller console
     */
    String dispatchConsoleCommandAndGetResult(CharSequence commandLine);

    /**
     * Starts a new queued process
     *
     * @param serverGroup       The server group of which the process should be started
     * @return The created dev process, containing all relevant information about the process
     */
    DevProcess startQueuedProcess(ServerGroup serverGroup);

    /**
     * Starts a new queued process
     *
     * @param serverGroup       The server group of which the process should be started
     * @param template          The template which should be used
     * @return The created dev process, containing all relevant information about the process
     */
    DevProcess startQueuedProcess(ServerGroup serverGroup, String template);

    /**
     * Starts a new queued process
     *
     * @param serverGroup       The server group of which the process should be started
     * @param template          The template which should be used
     * @param preConfig         The pre config which should be saved on the server
     * @return The created dev process, containing all relevant information about the process
     */
    DevProcess startQueuedProcess(ServerGroup serverGroup, String template, Configuration preConfig);

    /**
     * Get a online player of the cloudSystem
     *
     * @param uniqueId The exact UUID of the player
     * @return The OnlinePlayer or {@code null} if the player isn't online
     */
    OnlinePlayer getOnlinePlayer(UUID uniqueId);

    /**
     * Get a online player of the cloudSystem
     *
     * @param name The exact name of the player
     * @return The OnlinePlayer or {@code null} if the player isn't online
     */
    OnlinePlayer getOnlinePlayer(String name);

    /**
     * Get a offline player
     *
     * @param uniqueId The exact UUID of the player
     * @return The OfflinePlayer or {@code null} if the player isn't registered
     */
    OfflinePlayer getOfflinePlayer(UUID uniqueId);

    /**
     * Get a offline player
     *
     * @param name The exact name of the player
     * @return The OfflinePlayer or {@code null} if the player isn't registered
     */
    OfflinePlayer getOfflinePlayer(String name);

    /**
     * Updates a online player in the cloud system
     *
     * @param onlinePlayer The exact player that should be updated
     */
    void updateOnlinePlayer(OnlinePlayer onlinePlayer);

    /**
     * Updates a offline player in the cloud system
     *
     * @param offlinePlayer The exact player that should be updated
     */
    void updateOfflinePlayer(OfflinePlayer offlinePlayer);

    /**
     * Gets if the player is globally online
     *
     * @param uniqueId The exact UUID of the player which should be checked
     * @return {@code true} if the player is online or {@code false}
     * if the player is offline
     */
    boolean isOnline(UUID uniqueId);

    /**
     * Gets if the player is globally online
     *
     * @param name The exact name of the player which should be checked
     * @return {@code true} if the player is online or {@code false}
     * if the player is offline
     */
    boolean isOnline(String name);

    /**
     * Gets if the player is globally registered
     *
     * @param uniqueId The exact UUID of the player which should be checked
     * @return {@code true} if the player is registered or {@code false}
     * if the player isn't registered
     */
    boolean isRegistered(UUID uniqueId);

    /**
     * Gets if the player is globally registered
     *
     * @param name The exact name of the player which should be checked
     * @return {@code true} if the player is registered or {@code false}
     * if the player isn't registered
     */
    boolean isRegistered(String name);

    /**
     * Gets the max players of the current instance
     *
     * @return The max players of the current instance
     */
    int getMaxPlayers();

    /**
     * Returns the online count of the current instance
     *
     * @return the online count of the current instance
     */
    int getOnlineCount();

    /**
     * Returns the global online count
     *
     * @return the global online count
     */
    int getGlobalOnlineCount();

    /**
     * Gets all registered clients
     *
     * @return a list of all registered clients
     */
    List<Client> getAllClients();

    /**
     * Gets all connected clients
     *
     * @return a list of all connected clients
     */
    List<Client> getAllConnectedClients();

    /**
     * Gets all registered server groups
     *
     * @return a list of all registered server groups
     */
    List<ServerGroup> getAllServerGroups();

    /**
     * Gets all registered proxy groups
     *
     * @return a list of all registered proxy groups
     */
    List<ProxyGroup> getAllProxyGroups();

    /**
     * Gets all registered servers
     *
     * @return a list of all registered servers
     */
    List<ServerInfo> getAllRegisteredServers();

    /**
     * Gets all registered proxies
     *
     * @return a list of all registered proxies
     */
    List<ProxyInfo> getAllRegisteredProxies();

    /**
     * Gets all registered servers of a specific group
     *
     * @param groupName The name of the group that should be filtered
     * @return a list of all registered servers of a specific group
     */
    List<ServerInfo> getAllRegisteredServers(String groupName);

    /**
     * Gets all registered proxies of a specific group
     *
     * @param groupName The name of the group that should be filtered
     * @return a list of all registered proxies of a specific group
     */
    List<ProxyInfo> getAllRegisteredProxies(String groupName);

    /**
     * Sends a packet through the cloud system
     *
     * @param subChannel The instance name of the packet receiver
     * @param packet     The packet that should be send
     * @return If the operation was successful
     */
    boolean sendPacket(String subChannel, Packet packet);

    /**
     * Sends a packet through the cloud system
     *
     * @param subChannel The instance name of the packet receiver
     * @param packet     The packet that should be send
     * @return If the operation was successful
     */
    boolean sendPacketSync(String subChannel, Packet packet);

    /**
     * Sends a packet to all instances
     *
     * @param packet The packet that should be send
     */
    void sendPacketToAll(Packet packet);

    /**
     * Sends a packet to all instances
     *
     * @param packet The packet that should be send
     */
    void sendPacketToAllSync(Packet packet);

    /**
     * Sends a packet query through the cloud system
     *
     * @param channel   The instance name of the packet receiver
     * @param packet    The packet that should be send
     * @param onSuccess The handler when the query returns another packet
     */
    void sendPacketQuery(String channel, Packet packet, NetworkQueryInboundHandler onSuccess);

    /**
     * Sends a packet query through the cloud system
     *
     * @param channel   The instance name of the packet receiver
     * @param packet    The packet that should be send
     * @param onSuccess The handler when the query returns another packet
     * @param onFailure The handler when the query fails
     */
    void sendPacketQuery(String channel, Packet packet, NetworkQueryInboundHandler onSuccess, NetworkQueryInboundHandler onFailure);

    /**
     * Creates a packet future
     *
     * @param packet           The packet that should be send
     * @param networkComponent The instance name of the packet receiver
     * @return The created packet future
     */
    PacketFuture createPacketFuture(Packet packet, String networkComponent);

    /**
     * Creates a packet future
     *
     * @param packet  The packet that should be send
     * @param channel The instance name of the packet receiver
     * @return The created packet future
     */
    PacketFuture sendPacketQuery(String channel, Packet packet);

    /**
     * Gets a specific client
     *
     * @param name The name of the client
     * @return The client or {@code null} if the client doesn't exists
     */
    Client getClient(String name);

    /**
     * Gets a specific client
     *
     * @param name The name of the client
     * @return The client or {@code null} if the client isn't connected
     */
    ClientInfo getConnectedClient(String name);

    /**
     * Gets the server info of a specific server
     *
     * @param uniqueID The process UID
     * @return The serverInfo or {@code null} if the server couldn't be found
     */
    ServerInfo getServerInfo(UUID uniqueID);

    /**
     * Gets the server info of a specific server
     *
     * @param name The process name
     * @return The serverInfo or {@code null} if the server couldn't be found
     */
    ServerInfo getServerInfo(String name);

    /**
     * Gets the proxy info of a specific proxy
     *
     * @param uniqueID The process UID
     * @return The serverInfo or {@code null} if the proxy couldn't be found
     */
    ProxyInfo getProxyInfo(UUID uniqueID);

    /**
     * Gets the proxy info of a specific proxy
     *
     * @param name The process name
     * @return The serverInfo or {@code null} if the proxy couldn't be found
     */
    ProxyInfo getProxyInfo(String name);

    /**
     * Gets a specific serverGroup
     *
     * @param name The name of the serverGroup
     * @return The serverGroup or {@code null} if the serverGroup doesn't exists
     */
    ServerGroup getServerGroup(String name);

    /**
     * Gets a specific proxyGroup
     *
     * @param name The name of the proxyGroup
     * @return The serverGroup or {@code null} if the proxyGroup doesn't exists
     */
    ProxyGroup getProxyGroup(String name);

    /**
     * Gets the global netty handler
     *
     * @return The global netty handler
     */
    NettyHandler getNettyHandler();

    /**
     * Get the save instance of the api
     *
     * @return the save instance of the api
     */
    Optional<ISaveAPIService> getAPISave();

    /**
     * Closes the current process
     */
    void removeInternalProcess();
}
