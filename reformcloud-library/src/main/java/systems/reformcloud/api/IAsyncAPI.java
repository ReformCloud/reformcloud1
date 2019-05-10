/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.api.helper.IAsyncAPIHelper;
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

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author _Klaro | Pasqual K. / created on 25.04.2019
 */

public final class IAsyncAPI implements Serializable, IAsyncAPIHelper {
    private static AtomicReference<IAsyncAPI> instance = new AtomicReference<>();

    public IAsyncAPI() {
        instance.set(this);
    }

    public static AtomicReference<IAsyncAPI> getInstance() {
        return instance;
    }

    @Override
    public void startGameServer(String serverGroupName) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().startGameServer(serverGroupName));
    }

    @Override
    public void startGameServer(String serverGroupName, Configuration preConfig) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().startGameServer(serverGroupName, preConfig));
    }

    @Override
    public void startGameServer(ServerGroup serverGroup) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().startGameServer(serverGroup));
    }

    @Override
    public void startGameServer(ServerGroup serverGroup, Configuration preConfig) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().startGameServer(serverGroup, preConfig));
    }

    @Override
    public void startGameServer(ServerGroup serverGroup, Configuration preConfig, String template) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().startGameServer(serverGroup, preConfig, template));
    }

    @Override
    public void startProxy(String proxyGroupName) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().startProxy(proxyGroupName));
    }

    @Override
    public void startProxy(String proxyGroupName, Configuration preConfig) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().startProxy(proxyGroupName, preConfig));
    }

    @Override
    public void startProxy(ProxyGroup proxyGroup) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().startProxy(proxyGroup));
    }

    @Override
    public void startProxy(ProxyGroup proxyGroup, Configuration preConfig) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().startProxy(proxyGroup, preConfig));
    }

    @Override
    public void startProxy(ProxyGroup proxyGroup, Configuration preConfig, String template) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().startProxy(proxyGroup, preConfig, template));
    }

    @Override
    public boolean stopProxy(String name) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().stopProxy(name));
        return true;
    }

    @Override
    public boolean stopProxy(ProxyInfo proxyInfo) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().stopProxy(proxyInfo));
        return true;
    }

    @Override
    public boolean stopServer(String name) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().stopServer(name));
        return true;
    }

    @Override
    public boolean stopServer(ServerInfo serverInfo) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().stopServer(serverInfo));
        return true;
    }

    @Override
    public void createServerGroup(String name, ServerModeType serverModeType, Collection<String> clients, SpigotVersions spigotVersions) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().createServerGroup(name, serverModeType, clients, spigotVersions));
    }

    @Override
    public void createServerGroup(String name) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().createServerGroup(name));
    }

    @Override
    public void createServerGroup(String name, ServerModeType serverModeType, Collection<Template> templates) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().createServerGroup(name, serverModeType, templates));
    }

    @Override
    public void createServerGroup(String name, ServerModeType serverModeType, Collection<String> clients, Collection<Template> templates, SpigotVersions spigotVersions) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().createServerGroup(name, serverModeType, clients, templates, spigotVersions));
    }

    @Override
    public void createProxyGroup(String name, ProxyModeType proxyModeType, Collection<String> clients, ProxyVersions proxyVersions) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().createProxyGroup(name, proxyModeType, clients, proxyVersions));
    }

    @Override
    public void createProxyGroup(String name) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().createProxyGroup(name));
    }

    @Override
    public void createProxyGroup(String name, ProxyModeType proxyModeType, Collection<Template> templates) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().createProxyGroup(name, proxyModeType, templates));
    }

    @Override
    public void createProxyGroup(String name, ProxyModeType proxyModeType, Collection<String> clients, Collection<Template> templates, ProxyVersions proxyVersions) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().createProxyGroup(name, proxyModeType, clients, templates, proxyVersions));
    }

    @Override
    public void createClient(String name, String host) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().createClient(name, host));
    }

    @Override
    public void createClient(String name) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().createClient(name));
    }

    @Override
    public void updateServerInfo(ServerInfo serverInfo) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().updateServerInfo(serverInfo));
    }

    @Override
    public void updateProxyInfo(ProxyInfo proxyInfo) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().updateProxyInfo(proxyInfo));
    }

    @Override
    public void updateServerGroup(ServerGroup serverGroup) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().updateServerGroup(serverGroup));
    }

    @Override
    public void updateProxyGroup(ProxyGroup proxyGroup) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().updateProxyGroup(proxyGroup));
    }

    @Override
    public void createWebUser(String name) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().createWebUser(name));
    }

    @Override
    public void createWebUser(String name, String password) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().createWebUser(name, password));
    }

    @Override
    public void createWebUser(String name, String password, Map<String, Boolean> permissions) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().createWebUser(name, password, permissions));
    }

    @Override
    public void createWebUser(WebUser webUser) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().createWebUser(webUser));
    }

    @Override
    public void dispatchConsoleCommand(String commandLine) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().dispatchConsoleCommand(commandLine));
    }

    @Override
    public void dispatchConsoleCommand(CharSequence commandLine) {
        checkAvailable();
        CompletableFuture.runAsync(() -> dispatchConsoleCommand(String.valueOf(commandLine)));
    }

    @Override
    public CompletableFuture<String> dispatchConsoleCommandAndGetResult(String commandLine) {
        checkAvailable(true);
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().dispatchConsoleCommandAndGetResult(commandLine)));
        return completableFuture;
    }

    @Override
    public CompletableFuture<String> dispatchConsoleCommandAndGetResult(CharSequence commandLine) {
        return dispatchConsoleCommandAndGetResult(String.valueOf(commandLine));
    }

    @Override
    public CompletableFuture<DevProcess> startQueuedProcess(ServerGroup serverGroup) {
        checkAvailable(true);
        CompletableFuture<DevProcess> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().startQueuedProcess(serverGroup)));
        return completableFuture;
    }

    @Override
    public CompletableFuture<DevProcess> startQueuedProcess(ServerGroup serverGroup, String template) {
        checkAvailable(true);
        CompletableFuture<DevProcess> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().startQueuedProcess(serverGroup, template)));
        return completableFuture;
    }

    @Override
    public CompletableFuture<DevProcess> startQueuedProcess(ServerGroup serverGroup, String template, Configuration preConfig) {
        checkAvailable(true);
        CompletableFuture<DevProcess> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().startQueuedProcess(serverGroup, template, preConfig)));
        return completableFuture;
    }

    @Override
    public CompletableFuture<OnlinePlayer> getOnlinePlayer(UUID uniqueId) {
        checkAvailable(true);
        CompletableFuture<OnlinePlayer> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().getOnlinePlayer(uniqueId)));
        return completableFuture;
    }

    @Override
    public CompletableFuture<OnlinePlayer> getOnlinePlayer(String name) {
        checkAvailable(true);
        CompletableFuture<OnlinePlayer> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().getOnlinePlayer(name)));
        return completableFuture;
    }

    @Override
    public CompletableFuture<OfflinePlayer> getOfflinePlayer(UUID uniqueId) {
        checkAvailable(true);
        CompletableFuture<OfflinePlayer> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().getOfflinePlayer(uniqueId)));
        return completableFuture;
    }

    @Override
    public CompletableFuture<OfflinePlayer> getOfflinePlayer(String name) {
        checkAvailable(true);
        CompletableFuture<OfflinePlayer> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().getOfflinePlayer(name)));
        return completableFuture;
    }

    @Override
    public void updateOnlinePlayer(OnlinePlayer onlinePlayer) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().updateOnlinePlayer(onlinePlayer));
    }

    @Override
    public void updateOfflinePlayer(OfflinePlayer offlinePlayer) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().updateOfflinePlayer(offlinePlayer));
    }

    @Override
    public CompletableFuture<Boolean> isOnline(UUID uniqueId) {
        checkAvailable(true);
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().isOnline(uniqueId)));
        return completableFuture;
    }

    @Override
    public CompletableFuture<Boolean> isOnline(String name) {
        checkAvailable(true);
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().isOnline(name)));
        return completableFuture;
    }

    @Override
    public CompletableFuture<Boolean> isRegistered(UUID uniqueId) {
        checkAvailable(true);
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().isRegistered(uniqueId)));
        return completableFuture;
    }

    @Override
    public CompletableFuture<Boolean> isRegistered(String name) {
        checkAvailable(true);
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().isRegistered(name)));
        return completableFuture;
    }

    @Override
    public CompletableFuture<Integer> getMaxPlayers() {
        checkAvailable(true);
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().getMaxPlayers()));
        return completableFuture;
    }

    @Override
    public CompletableFuture<Integer> getOnlineCount() {
        checkAvailable(true);
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().getOnlineCount()));
        return completableFuture;
    }

    @Override
    public CompletableFuture<Integer> getGlobalOnlineCount() {
        checkAvailable(true);
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().getGlobalOnlineCount()));
        return completableFuture;
    }

    @Override
    public CompletableFuture<List<Client>> getAllClients() {
        checkAvailable(true);
        CompletableFuture<List<Client>> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().getAllClients()));
        return completableFuture;
    }

    @Override
    public CompletableFuture<List<Client>> getAllConnectedClients() {
        checkAvailable(true);
        CompletableFuture<List<Client>> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().getAllConnectedClients()));
        return completableFuture;
    }

    @Override
    public CompletableFuture<List<ServerGroup>> getAllServerGroups() {
        checkAvailable(true);
        CompletableFuture<List<ServerGroup>> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().getAllServerGroups()));
        return completableFuture;
    }

    @Override
    public CompletableFuture<List<ProxyGroup>> getAllProxyGroups() {
        checkAvailable(true);
        CompletableFuture<List<ProxyGroup>> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().getAllProxyGroups()));
        return completableFuture;
    }

    @Override
    public CompletableFuture<List<ServerInfo>> getAllRegisteredServers() {
        checkAvailable(true);
        CompletableFuture<List<ServerInfo>> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().getAllRegisteredServers()));
        return completableFuture;
    }

    @Override
    public CompletableFuture<List<ProxyInfo>> getAllRegisteredProxies() {
        checkAvailable(true);
        CompletableFuture<List<ProxyInfo>> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().getAllRegisteredProxies()));
        return completableFuture;
    }

    @Override
    public CompletableFuture<List<ServerInfo>> getAllRegisteredServers(String groupName) {
        checkAvailable(true);
        CompletableFuture<List<ServerInfo>> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().getAllRegisteredServers(groupName)));
        return completableFuture;
    }

    @Override
    public CompletableFuture<List<ProxyInfo>> getAllRegisteredProxies(String groupName) {
        checkAvailable(true);
        CompletableFuture<List<ProxyInfo>> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().getAllRegisteredProxies(groupName)));
        return completableFuture;
    }

    @Override
    public CompletableFuture<Boolean> sendPacket(String subChannel, Packet packet) {
        checkAvailable(true);
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().sendPacket(subChannel, packet)));
        return completableFuture;
    }

    @Override
    public CompletableFuture<Boolean> sendPacketSync(String subChannel, Packet packet) {
        checkAvailable(true);
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().sendPacketSync(subChannel, packet)));
        return completableFuture;
    }

    @Override
    public void sendPacketToAll(Packet packet) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().sendPacketToAll(packet));
    }

    @Override
    public void sendPacketToAllSync(Packet packet) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().sendPacketToAllSync(packet));
    }

    @Override
    public void sendPacketQuery(String channel, Packet packet, NetworkQueryInboundHandler onSuccess) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().sendPacketQuery(channel, packet, onSuccess));
    }

    @Override
    public void sendPacketQuery(String channel, Packet packet, NetworkQueryInboundHandler onSuccess, NetworkQueryInboundHandler onFailure) {
        checkAvailable();
        CompletableFuture.runAsync(() -> IAPIService.instance.get().sendPacketQuery(channel, packet, onSuccess, onFailure));
    }

    @Override
    public CompletableFuture<PacketFuture> createPacketFuture(Packet packet, String networkComponent) {
        checkAvailable(true);
        CompletableFuture<PacketFuture> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().createPacketFuture(packet, networkComponent)));
        return completableFuture;
    }

    @Override
    public CompletableFuture<PacketFuture> sendPacketQuery(String channel, Packet packet) {
        checkAvailable(true);
        CompletableFuture<PacketFuture> completableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> completableFuture.complete(IAPIService.instance.get().sendPacketQuery(channel, packet)));
        return completableFuture;
    }

    @Override
    public CompletableFuture<Client> getClient(String name) {
        checkAvailable(true);
        CompletableFuture<Client> clientCompletableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> clientCompletableFuture.complete(IAPIService.instance.get().getClient(name)));
        return clientCompletableFuture;
    }

    @Override
    public CompletableFuture<ClientInfo> getConnectedClient(String name) {
        checkAvailable(true);
        CompletableFuture<ClientInfo> clientCompletableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> clientCompletableFuture.complete(IAPIService.instance.get().getConnectedClient(name)));
        return clientCompletableFuture;
    }

    @Override
    public CompletableFuture<ServerInfo> getServerInfo(UUID uniqueID) {
        checkAvailable(true);
        CompletableFuture<ServerInfo> clientCompletableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> clientCompletableFuture.complete(IAPIService.instance.get().getServerInfo(uniqueID)));
        return clientCompletableFuture;
    }

    @Override
    public CompletableFuture<ServerInfo> getServerInfo(String name) {
        checkAvailable(true);
        CompletableFuture<ServerInfo> clientCompletableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> clientCompletableFuture.complete(IAPIService.instance.get().getServerInfo(name)));
        return clientCompletableFuture;
    }

    @Override
    public CompletableFuture<ProxyInfo> getProxyInfo(UUID uniqueID) {
        checkAvailable(true);
        CompletableFuture<ProxyInfo> clientCompletableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> clientCompletableFuture.complete(IAPIService.instance.get().getProxyInfo(uniqueID)));
        return clientCompletableFuture;
    }

    @Override
    public CompletableFuture<ProxyInfo> getProxyInfo(String name) {
        checkAvailable(true);
        CompletableFuture<ProxyInfo> clientCompletableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> clientCompletableFuture.complete(IAPIService.instance.get().getProxyInfo(name)));
        return clientCompletableFuture;
    }

    @Override
    public CompletableFuture<ServerGroup> getServerGroup(String name) {
        checkAvailable(true);
        CompletableFuture<ServerGroup> clientCompletableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> clientCompletableFuture.complete(IAPIService.instance.get().getServerGroup(name)));
        return clientCompletableFuture;
    }

    @Override
    public CompletableFuture<ProxyGroup> getProxyGroup(String name) {
        checkAvailable(true);
        CompletableFuture<ProxyGroup> clientCompletableFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> clientCompletableFuture.complete(IAPIService.instance.get().getProxyGroup(name)));
        return clientCompletableFuture;
    }

    private void checkAvailable() {
        checkAvailable(false);
    }

    private void checkAvailable(boolean canBeAsync) {
        if (IAPIService.instance.get() == null)
            throw new IllegalStateException("Cannot access api service, not initialized yet");

        if (!ReformCloudLibraryService.isOnMainThread() && !canBeAsync)
            throw new IllegalStateException("Cannot call an async method on an async thread");
    }
}
