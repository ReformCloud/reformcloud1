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
import systems.reformcloud.network.packet.Packet;

import java.util.List;
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

    Client getClient(String name);

    ClientInfo getConnectedClient(String name);

    ServerGroup getServerGroup(String name);

    ProxyGroup getProxyGroup(String name);

    NettyHandler getNettyHandler();

    void removeInternalProcess();
}
