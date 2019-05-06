/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api.helper;

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
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.network.packet.PacketFuture;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.player.implementations.OnlinePlayer;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author _Klaro | Pasqual K. / created on 25.04.2019
 */

public interface IAsyncAPIHelper {
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

    boolean stopProxy(String name);

    boolean stopProxy(ProxyInfo proxyInfo);

    boolean stopServer(String name);

    boolean stopServer(ServerInfo serverInfo);

    void createServerGroup(String name, ServerModeType serverModeType, Collection<String> clients, SpigotVersions spigotVersions);

    void createServerGroup(String name);

    void createServerGroup(String name, ServerModeType serverModeType, Collection<Template> templates);

    void createServerGroup(String name, ServerModeType serverModeType, Collection<String> clients, Collection<Template> templates, SpigotVersions spigotVersions);

    void createProxyGroup(String name, ProxyModeType proxyModeType, Collection<String> clients, ProxyVersions proxyVersions);

    void createProxyGroup(String name);

    void createProxyGroup(String name, ProxyModeType proxyModeType, Collection<Template> templates);

    void createProxyGroup(String name, ProxyModeType proxyModeType, Collection<String> clients, Collection<Template> templates, ProxyVersions proxyVersions);

    void createClient(String name, String host);

    void createClient(String name);

    void updateServerInfo(ServerInfo serverInfo);

    void updateProxyInfo(ProxyInfo proxyInfo);

    void updateServerGroup(ServerGroup serverGroup);

    void updateProxyGroup(ProxyGroup proxyGroup);

    void createWebUser(String name);

    void createWebUser(String name, String password);

    void createWebUser(String name, String password, Map<String, Boolean> permissions);

    void createWebUser(WebUser webUser);

    void dispatchConsoleCommand(String commandLine);

    void dispatchConsoleCommand(CharSequence commandLine);

    CompletableFuture<String> dispatchConsoleCommandAndGetResult(String commandLine);

    CompletableFuture<String> dispatchConsoleCommandAndGetResult(CharSequence commandLine);

    CompletableFuture<DevProcess> startQueuedProcess(ServerGroup serverGroup);

    CompletableFuture<DevProcess> startQueuedProcess(ServerGroup serverGroup, String template);

    CompletableFuture<DevProcess> startQueuedProcess(ServerGroup serverGroup, String template, Configuration preConfig);

    CompletableFuture<OnlinePlayer> getOnlinePlayer(UUID uniqueId);

    CompletableFuture<OnlinePlayer> getOnlinePlayer(String name);

    CompletableFuture<OfflinePlayer> getOfflinePlayer(UUID uniqueId);

    CompletableFuture<OfflinePlayer> getOfflinePlayer(String name);

    void updateOnlinePlayer(OnlinePlayer onlinePlayer);

    void updateOfflinePlayer(OfflinePlayer offlinePlayer);

    CompletableFuture<Boolean> isOnline(UUID uniqueId);

    CompletableFuture<Boolean> isOnline(String name);

    CompletableFuture<Boolean> isRegistered(UUID uniqueId);

    CompletableFuture<Boolean> isRegistered(String name);

    CompletableFuture<Integer> getMaxPlayers();

    CompletableFuture<Integer> getOnlineCount();

    CompletableFuture<Integer> getGlobalOnlineCount();

    CompletableFuture<List<Client>> getAllClients();

    CompletableFuture<List<Client>> getAllConnectedClients();

    CompletableFuture<List<ServerGroup>> getAllServerGroups();

    CompletableFuture<List<ProxyGroup>> getAllProxyGroups();

    CompletableFuture<List<ServerInfo>> getAllRegisteredServers();

    CompletableFuture<List<ProxyInfo>> getAllRegisteredProxies();

    CompletableFuture<List<ServerInfo>> getAllRegisteredServers(String groupName);

    CompletableFuture<List<ProxyInfo>> getAllRegisteredProxies(String groupName);

    CompletableFuture<Boolean> sendPacket(String subChannel, Packet packet);

    CompletableFuture<Boolean> sendPacketSync(String subChannel, Packet packet);

    void sendPacketToAll(Packet packet);

    void sendPacketToAllSync(Packet packet);

    void sendPacketQuery(String channel, Packet packet, NetworkQueryInboundHandler onSuccess);

    void sendPacketQuery(String channel, Packet packet, NetworkQueryInboundHandler onSuccess, NetworkQueryInboundHandler onFailure);

    CompletableFuture<PacketFuture> createPacketFuture(Packet packet, String networkComponent);

    CompletableFuture<PacketFuture> sendPacketQuery(String channel, Packet packet);

    CompletableFuture<Client> getClient(String name);

    CompletableFuture<ClientInfo> getConnectedClient(String name);

    CompletableFuture<ServerInfo> getServerInfo(UUID uniqueID);

    CompletableFuture<ServerInfo> getServerInfo(String name);

    CompletableFuture<ProxyInfo> getProxyInfo(UUID uniqueID);

    CompletableFuture<ProxyInfo> getProxyInfo(String name);

    CompletableFuture<ServerGroup> getServerGroup(String name);

    CompletableFuture<ProxyGroup> getProxyGroup(String name);
}
