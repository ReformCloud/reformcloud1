/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.network.NettyHandler;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.network.packet.PacketFuture;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.player.implementations.OnlinePlayer;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author _Klaro | Pasqual K. / created on 17.02.2019
 */

public interface IAPIService {
    AtomicReference<IAPIService> instance = new AtomicReference<>();

    void startGameServer(String serverGroupName);

    void startGameServer(String serverGroupName, Configuration preConfig);

    void startGameServer(ServerGroup serverGroup);

    void startGameServer(ServerGroup serverGroup, Configuration preConfig);

    void startGameServer(ServerGroup serverGroup, Configuration preConfig, String template);

    void startProxy(String proxyGroupName);

    void startProxy(String proxyGroupName, Configuration preConfig);

    void startProxy(ProxyGroup proxyGroup);

    void startProxy(ProxyGroup proxyGroup, Configuration preConfig);

    void startProxy(ProxyGroup proxyGroup, Configuration preConfig, String template);

    OnlinePlayer getOnlinePlayer(UUID uniqueId);

    OnlinePlayer getOnlinePlayer(String name);

    OfflinePlayer getOfflinePlayer(UUID uniqueId);

    OfflinePlayer getOfflinePlayer(String name);

    void updateOnlinePlayer(OnlinePlayer onlinePlayer);

    void updateOfflinePlayer(OfflinePlayer offlinePlayer);

    boolean isOnline(UUID uniqueId);

    boolean isOnline(String name);

    boolean isRegistered(UUID uniqueId);

    boolean isRegistered(String name);

    int getMaxPlayers();

    int getOnlineCount();

    List<Client> getAllClients();

    List<Client> getAllConnectedClients();

    List<ServerGroup> getAllServerGroups();

    List<ProxyGroup> getAllProxyGroups();

    List<ServerInfo> getAllRegisteredServers();

    List<ProxyInfo> getAllRegisteredProxies();

    List<ServerInfo> getAllRegisteredServers(String groupName);

    List<ProxyInfo> getAllRegisteredProxies(String groupName);

    boolean sendPacket(String subChannel, Packet packet);

    boolean sendPacketSync(String subChannel, Packet packet);

    void sendPacketToAll(Packet packet);

    void sendPacketToAllSync(Packet packet);

    void sendPacketQuery(String channel, Packet packet, NetworkQueryInboundHandler onSuccess);

    void sendPacketQuery(String channel, Packet packet, NetworkQueryInboundHandler onSuccess, NetworkQueryInboundHandler onFailure);

    PacketFuture createPacketFuture(Packet packet, String networkComponent);

    PacketFuture sendPacketQuery(String channel, Packet packet);

    Client getClient(String name);

    ClientInfo getConnectedClient(String name);

    ServerInfo getServerInfo(UUID uniqueID);

    ServerInfo getServerInfo(String name);

    ProxyInfo getProxyInfo(UUID uniqueID);

    ProxyInfo getProxyInfo(String name);

    ServerGroup getServerGroup(String name);

    ProxyGroup getProxyGroup(String name);

    NettyHandler getNettyHandler();

    void removeInternalProcess();
}
