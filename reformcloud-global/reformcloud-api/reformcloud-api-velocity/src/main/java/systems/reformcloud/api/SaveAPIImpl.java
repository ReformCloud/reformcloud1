/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api;

import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.api.save.ISaveAPIService;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.network.NettyHandler;
import systems.reformcloud.network.packet.Packet;
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

public final class SaveAPIImpl implements Serializable, ISaveAPIService {
    @Override
    public Optional<OnlinePlayer> getOnlinePlayer(UUID uniqueId) {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().getOnlinePlayer(uniqueId));
    }

    @Override
    public Optional<OnlinePlayer> getOnlinePlayer(String name) {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().getOnlinePlayer(name));
    }

    @Override
    public Optional<OfflinePlayer> getOfflinePlayer(UUID uniqueId) {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().getOfflinePlayer(uniqueId));
    }

    @Override
    public Optional<OfflinePlayer> getOfflinePlayer(String name) {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().getOfflinePlayer(name));
    }

    @Override
    public Optional<Boolean> isOnline(UUID uniqueId) {
        return Optional.of(ReformCloudAPIVelocity.getInstance().isOnline(uniqueId));
    }

    @Override
    public Optional<Boolean> isOnline(String name) {
        return Optional.of(ReformCloudAPIVelocity.getInstance().isOnline(name));
    }

    @Override
    public Optional<Boolean> isRegistered(UUID uniqueId) {
        return Optional.of(ReformCloudAPIVelocity.getInstance().isRegistered(uniqueId));
    }

    @Override
    public Optional<Boolean> isRegistered(String name) {
        return Optional.of(ReformCloudAPIVelocity.getInstance().isRegistered(name));
    }

    @Override
    public Optional<Integer> getMaxPlayers() {
        return Optional.of(ReformCloudAPIVelocity.getInstance().getMaxPlayers());
    }

    @Override
    public Optional<Integer> getOnlineCount() {
        return Optional.of(ReformCloudAPIVelocity.getInstance().getOnlineCount());
    }

    @Override
    public Optional<Integer> getGlobalOnlineCount() {
        return Optional.of(ReformCloudAPIVelocity.getInstance().getGlobalOnlineCount());
    }

    @Override
    public Optional<List<Client>> getAllClients() {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().getAllClients());
    }

    @Override
    public Optional<List<Client>> getAllConnectedClients() {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().getAllConnectedClients());
    }

    @Override
    public Optional<List<ServerGroup>> getAllServerGroups() {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().getAllServerGroups());
    }

    @Override
    public Optional<List<ProxyGroup>> getAllProxyGroups() {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().getAllProxyGroups());
    }

    @Override
    public Optional<List<ServerInfo>> getAllRegisteredServers() {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().getAllRegisteredServers());
    }

    @Override
    public Optional<List<ProxyInfo>> getAllRegisteredProxies() {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().getAllRegisteredProxies());
    }

    @Override
    public Optional<List<ServerInfo>> getAllRegisteredServers(String groupName) {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().getAllRegisteredServers(groupName));
    }

    @Override
    public Optional<List<ProxyInfo>> getAllRegisteredProxies(String groupName) {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().getAllRegisteredProxies(groupName));
    }

    @Override
    public Optional<Boolean> sendPacket(String subChannel, Packet packet) {
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> sendPacketSync(String subChannel, Packet packet) {
        return Optional.of(ReformCloudAPIVelocity.getInstance().sendPacketSync(subChannel, packet));
    }

    @Override
    public Optional<PacketFuture> createPacketFuture(Packet packet, String networkComponent) {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().createPacketFuture(packet, networkComponent));
    }

    @Override
    public Optional<PacketFuture> sendPacketQuery(String channel, Packet packet) {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().sendPacketQuery(channel, packet));
    }

    @Override
    public Optional<Client> getClient(String name) {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().getClient(name));
    }

    @Override
    public Optional<ClientInfo> getConnectedClient(String name) {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().getConnectedClient(name));
    }

    @Override
    public Optional<ServerInfo> getServerInfo(UUID uniqueID) {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().getServerInfo(uniqueID));
    }

    @Override
    public Optional<ServerInfo> getServerInfo(String name) {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().getServerInfo(name));
    }

    @Override
    public Optional<ProxyInfo> getProxyInfo(UUID uniqueID) {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().getProxyInfo(uniqueID));
    }

    @Override
    public Optional<ProxyInfo> getProxyInfo(String name) {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().getProxyInfo(name));
    }

    @Override
    public Optional<ServerGroup> getServerGroup(String name) {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().getServerGroup(name));
    }

    @Override
    public Optional<ProxyGroup> getProxyGroup(String name) {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().getProxyGroup(name));
    }

    @Override
    public Optional<NettyHandler> getNettyHandler() {
        return Optional.ofNullable(ReformCloudAPIVelocity.getInstance().getNettyHandler());
    }
}
