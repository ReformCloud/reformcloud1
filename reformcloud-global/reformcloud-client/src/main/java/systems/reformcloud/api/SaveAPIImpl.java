/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.api.save.SaveAPIService;
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

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 30.03.2019
 */

public final class SaveAPIImpl implements Serializable, SaveAPIService {

    @Override
    public Optional<OnlinePlayer> getOnlinePlayer(UUID uniqueId) {
        return Optional.ofNullable(ReformCloudClient.getInstance().getOnlinePlayer(uniqueId));
    }

    @Override
    public Optional<OnlinePlayer> getOnlinePlayer(String name) {
        return Optional.ofNullable(ReformCloudClient.getInstance().getOnlinePlayer(name));
    }

    @Override
    public Optional<OfflinePlayer> getOfflinePlayer(UUID uniqueId) {
        return Optional.ofNullable(ReformCloudClient.getInstance().getOfflinePlayer(uniqueId));
    }

    @Override
    public Optional<OfflinePlayer> getOfflinePlayer(String name) {
        return Optional.ofNullable(ReformCloudClient.getInstance().getOfflinePlayer(name));
    }

    @Override
    public Optional<Boolean> isOnline(UUID uniqueId) {
        return Optional.of(ReformCloudClient.getInstance().isOnline(uniqueId));
    }

    @Override
    public Optional<Boolean> isOnline(String name) {
        return Optional.of(ReformCloudClient.getInstance().isOnline(name));
    }

    @Override
    public Optional<Boolean> isRegistered(UUID uniqueId) {
        return Optional.of(ReformCloudClient.getInstance().isRegistered(uniqueId));
    }

    @Override
    public Optional<Boolean> isRegistered(String name) {
        return Optional.of(ReformCloudClient.getInstance().isRegistered(name));
    }

    @Override
    public Optional<Integer> getMaxPlayers() {
        return Optional.of(ReformCloudClient.getInstance().getMaxPlayers());
    }

    @Override
    public Optional<Integer> getOnlineCount() {
        return Optional.of(ReformCloudClient.getInstance().getOnlineCount());
    }

    @Override
    public Optional<Integer> getGlobalOnlineCount() {
        return Optional.of(ReformCloudClient.getInstance().getGlobalOnlineCount());
    }

    @Override
    public Optional<List<Client>> getAllClients() {
        return Optional.of(ReformCloudClient.getInstance().getAllClients());
    }

    @Override
    public Optional<List<Client>> getAllConnectedClients() {
        return Optional.ofNullable(ReformCloudClient.getInstance().getAllConnectedClients());
    }

    @Override
    public Optional<List<ServerGroup>> getAllServerGroups() {
        return Optional.of(ReformCloudClient.getInstance().getAllServerGroups());
    }

    @Override
    public Optional<List<ProxyGroup>> getAllProxyGroups() {
        return Optional.of(ReformCloudClient.getInstance().getAllProxyGroups());
    }

    @Override
    public Optional<List<ServerInfo>> getAllRegisteredServers() {
        return Optional.of(ReformCloudClient.getInstance().getAllRegisteredServers());
    }

    @Override
    public Optional<List<ProxyInfo>> getAllRegisteredProxies() {
        return Optional.of(ReformCloudClient.getInstance().getAllRegisteredProxies());
    }

    @Override
    public Optional<List<ServerInfo>> getAllRegisteredServers(String groupName) {
        return Optional
            .of(ReformCloudClient.getInstance().getAllRegisteredServers(groupName));
    }

    @Override
    public Optional<List<ProxyInfo>> getAllRegisteredProxies(String groupName) {
        return Optional
            .ofNullable(ReformCloudClient.getInstance().getAllRegisteredProxies(groupName));
    }

    @Override
    public Optional<Boolean> sendPacket(String subChannel, DefaultPacket packet) {
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> sendPacketSync(String subChannel, DefaultPacket packet) {
        return Optional.of(ReformCloudClient.getInstance().sendPacketSync(subChannel, packet));
    }

    @Override
    public Optional<PacketFuture> createPacketFuture(DefaultPacket packet, String networkComponent) {
        return Optional.ofNullable(
            ReformCloudClient.getInstance().createPacketFuture(packet, networkComponent));
    }

    @Override
    public Optional<PacketFuture> sendPacketQuery(String channel, DefaultPacket packet) {
        return Optional
            .ofNullable(ReformCloudClient.getInstance().sendPacketQuery(channel, packet));
    }

    @Override
    public Optional<Client> getClient(String name) {
        return Optional.ofNullable(ReformCloudClient.getInstance().getClient(name));
    }

    @Override
    public Optional<ClientInfo> getConnectedClient(String name) {
        return Optional.ofNullable(ReformCloudClient.getInstance().getConnectedClient(name));
    }

    @Override
    public Optional<ServerInfo> getServerInfo(UUID uniqueID) {
        return Optional.ofNullable(ReformCloudClient.getInstance().getServerInfo(uniqueID));
    }

    @Override
    public Optional<ServerInfo> getServerInfo(String name) {
        return Optional.ofNullable(ReformCloudClient.getInstance().getServerInfo(name));
    }

    @Override
    public Optional<ProxyInfo> getProxyInfo(UUID uniqueID) {
        return Optional.ofNullable(ReformCloudClient.getInstance().getProxyInfo(uniqueID));
    }

    @Override
    public Optional<ProxyInfo> getProxyInfo(String name) {
        return Optional.ofNullable(ReformCloudClient.getInstance().getProxyInfo(name));
    }

    @Override
    public Optional<ServerGroup> getServerGroup(String name) {
        return Optional.ofNullable(ReformCloudClient.getInstance().getServerGroup(name));
    }

    @Override
    public Optional<ProxyGroup> getProxyGroup(String name) {
        return Optional.ofNullable(ReformCloudClient.getInstance().getProxyGroup(name));
    }

    @Override
    public Optional<NettyHandler> getNettyHandler() {
        return Optional.ofNullable(ReformCloudClient.getInstance().getNettyHandler());
    }
}
