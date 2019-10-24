/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api.save;

import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.network.NettyHandler;
import systems.reformcloud.network.packet.DefaultPacket;
import systems.reformcloud.network.packet.PacketFuture;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.player.implementations.OnlinePlayer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author _Klaro | Pasqual K. / created on 29.03.2019
 */

public interface SaveAPIService {

    /**
     * The atomic reference to get the default instance of the api service
     */
    AtomicReference<SaveAPIService> instance = new AtomicReference<>();

    /**
     * Get a online player of the cloudSystem
     *
     * @param uniqueId The exact UUID of the player
     * @return The OnlinePlayer or {@code null} if the player isn't online
     */
    Optional<OnlinePlayer> getOnlinePlayer(UUID uniqueId);

    /**
     * Get a online player of the cloudSystem
     *
     * @param name The exact name of the player
     * @return The OnlinePlayer or {@code null} if the player isn't online
     */
    Optional<OnlinePlayer> getOnlinePlayer(String name);

    /**
     * Get a offline player
     *
     * @param uniqueId The exact UUID of the player
     * @return The OfflinePlayer or {@code null} if the player isn't registered
     */
    Optional<OfflinePlayer> getOfflinePlayer(UUID uniqueId);

    /**
     * Get a offline player
     *
     * @param name The exact name of the player
     * @return The OfflinePlayer or {@code null} if the player isn't registered
     */
    Optional<OfflinePlayer> getOfflinePlayer(String name);

    /**
     * Gets if the player is globally online
     *
     * @param uniqueId The exact UUID of the player which should be checked
     * @return {@code true} if the player is online or {@code false} if the player is offline
     */
    Optional<Boolean> isOnline(UUID uniqueId);

    /**
     * Gets if the player is globally online
     *
     * @param name The exact name of the player which should be checked
     * @return {@code true} if the player is online or {@code false} if the player is offline
     */
    Optional<Boolean> isOnline(String name);

    /**
     * Gets if the player is globally registered
     *
     * @param uniqueId The exact UUID of the player which should be checked
     * @return {@code true} if the player is registered or {@code false} if the player isn't
     * registered
     */
    Optional<Boolean> isRegistered(UUID uniqueId);

    /**
     * Gets if the player is globally registered
     *
     * @param name The exact name of the player which should be checked
     * @return {@code true} if the player is registered or {@code false} if the player isn't
     * registered
     */
    Optional<Boolean> isRegistered(String name);

    /**
     * Gets the max players of the current instance
     *
     * @return The max players of the current instance
     */
    Optional<Integer> getMaxPlayers();

    /**
     * Returns the online count of the current instance
     *
     * @return the online count of the current instance
     */
    Optional<Integer> getOnlineCount();

    /**
     * Returns the global online count
     *
     * @return the global online count
     */
    Optional<Integer> getGlobalOnlineCount();

    /**
     * Gets all registered clients
     *
     * @return a list of all registered clients
     */
    Optional<List<Client>> getAllClients();

    /**
     * Gets all connected clients
     *
     * @return a list of all connected clients
     */
    Optional<List<Client>> getAllConnectedClients();

    /**
     * Gets all registered server groups
     *
     * @return a list of all registered server groups
     */
    Optional<List<ServerGroup>> getAllServerGroups();

    /**
     * Gets all registered proxy groups
     *
     * @return a list of all registered proxy groups
     */
    Optional<List<ProxyGroup>> getAllProxyGroups();

    /**
     * Gets all registered servers
     *
     * @return a list of all registered servers
     */
    Optional<List<ServerInfo>> getAllRegisteredServers();

    /**
     * Gets all registered proxies
     *
     * @return a list of all registered proxies
     */
    Optional<List<ProxyInfo>> getAllRegisteredProxies();

    /**
     * Gets all registered servers of a specific group
     *
     * @param groupName The name of the group that should be filtered
     * @return a list of all registered servers of a specific group
     */
    Optional<List<ServerInfo>> getAllRegisteredServers(String groupName);

    /**
     * Gets all registered proxies of a specific group
     *
     * @param groupName The name of the group that should be filtered
     * @return a list of all registered proxies of a specific group
     */
    Optional<List<ProxyInfo>> getAllRegisteredProxies(String groupName);

    /**
     * Sends a packet through the cloud system
     *
     * @param subChannel The instance name of the packet receiver
     * @param packet The packet that should be send
     * @return If the operation was successful
     */
    Optional<Boolean> sendPacket(String subChannel, DefaultPacket packet);

    /**
     * Sends a packet through the cloud system
     *
     * @param subChannel The instance name of the packet receiver
     * @param packet The packet that should be send
     * @return If the operation was successful
     */
    Optional<Boolean> sendPacketSync(String subChannel, DefaultPacket packet);

    /**
     * Creates a packet future
     *
     * @param packet The packet that should be send
     * @param networkComponent The instance name of the packet receiver
     * @return The created packet future
     */
    Optional<PacketFuture> createPacketFuture(DefaultPacket packet, String networkComponent);

    /**
     * Creates a packet future
     *
     * @param packet The packet that should be send
     * @param channel The instance name of the packet receiver
     * @return The created packet future
     */
    Optional<PacketFuture> sendPacketQuery(String channel, DefaultPacket packet);

    /**
     * Gets a specific client
     *
     * @param name The name of the client
     * @return The client or {@code null} if the client doesn't exists
     */
    Optional<Client> getClient(String name);

    /**
     * Gets a specific client
     *
     * @param name The name of the client
     * @return The client or {@code null} if the client isn't connected
     */
    Optional<ClientInfo> getConnectedClient(String name);

    /**
     * Gets the server info of a specific server
     *
     * @param uniqueID The process UID
     * @return The serverInfo or {@code null} if the server couldn't be found
     */
    Optional<ServerInfo> getServerInfo(UUID uniqueID);

    /**
     * Gets the server info of a specific server
     *
     * @param name The process name
     * @return The serverInfo or {@code null} if the server couldn't be found
     */
    Optional<ServerInfo> getServerInfo(String name);

    /**
     * Gets the proxy info of a specific proxy
     *
     * @param uniqueID The process UID
     * @return The serverInfo or {@code null} if the proxy couldn't be found
     */
    Optional<ProxyInfo> getProxyInfo(UUID uniqueID);

    /**
     * Gets the proxy info of a specific proxy
     *
     * @param name The process name
     * @return The serverInfo or {@code null} if the proxy couldn't be found
     */
    Optional<ProxyInfo> getProxyInfo(String name);

    /**
     * Gets a specific serverGroup
     *
     * @param name The name of the serverGroup
     * @return The serverGroup or {@code null} if the serverGroup doesn't exists
     */
    Optional<ServerGroup> getServerGroup(String name);

    /**
     * Gets a specific proxyGroup
     *
     * @param name The name of the proxyGroup
     * @return The serverGroup or {@code null} if the proxyGroup doesn't exists
     */
    Optional<ProxyGroup> getProxyGroup(String name);

    /**
     * Gets the global netty handler
     *
     * @return The global netty handler
     */
    Optional<NettyHandler> getNettyHandler();
}
