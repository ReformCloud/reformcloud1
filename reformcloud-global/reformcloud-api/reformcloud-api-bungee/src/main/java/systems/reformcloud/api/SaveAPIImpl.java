/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api;

import systems.reformcloud.ReformCloudAPIBungee;
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
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().getOnlinePlayer(uniqueId));
    }

    @Override
    public Optional<OnlinePlayer> getOnlinePlayer(String name) {
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().getOnlinePlayer(name));
    }

    @Override
    public Optional<OfflinePlayer> getOfflinePlayer(UUID uniqueId) {
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().getOfflinePlayer(uniqueId));
    }

    @Override
    public Optional<OfflinePlayer> getOfflinePlayer(String name) {
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().getOfflinePlayer(name));
    }

    @Override
    public Optional<Boolean> isOnline(UUID uniqueId) {
        return Optional.of(ReformCloudAPIBungee.getInstance().isOnline(uniqueId));
    }

    @Override
    public Optional<Boolean> isOnline(String name) {
        return Optional.of(ReformCloudAPIBungee.getInstance().isOnline(name));
    }

    @Override
    public Optional<Boolean> isRegistered(UUID uniqueId) {
        return Optional.of(ReformCloudAPIBungee.getInstance().isRegistered(uniqueId));
    }

    @Override
    public Optional<Boolean> isRegistered(String name) {
        return Optional.of(ReformCloudAPIBungee.getInstance().isRegistered(name));
    }

    @Override
    public Optional<Integer> getMaxPlayers() {
        return Optional.of(ReformCloudAPIBungee.getInstance().getMaxPlayers());
    }

    @Override
    public Optional<Integer> getOnlineCount() {
        return Optional.of(ReformCloudAPIBungee.getInstance().getOnlineCount());
    }

    @Override
    public Optional<Integer> getGlobalOnlineCount() {
        return Optional.of(ReformCloudAPIBungee.getInstance().getGlobalOnlineCount());
    }

    @Override
    public Optional<List<Client>> getAllClients() {
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().getAllClients());
    }

    @Override
    public Optional<List<Client>> getAllConnectedClients() {
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().getAllConnectedClients());
    }

    @Override
    public Optional<List<ServerGroup>> getAllServerGroups() {
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().getAllServerGroups());
    }

    @Override
    public Optional<List<ProxyGroup>> getAllProxyGroups() {
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().getAllProxyGroups());
    }

    @Override
    public Optional<List<ServerInfo>> getAllRegisteredServers() {
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().getAllRegisteredServers());
    }

    @Override
    public Optional<List<ProxyInfo>> getAllRegisteredProxies() {
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().getAllRegisteredProxies());
    }

    @Override
    public Optional<List<ServerInfo>> getAllRegisteredServers(String groupName) {
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().getAllRegisteredServers(groupName));
    }

    @Override
    public Optional<List<ProxyInfo>> getAllRegisteredProxies(String groupName) {
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().getAllRegisteredProxies(groupName));
    }

    @Override
    public Optional<Boolean> sendPacket(String subChannel, Packet packet) {
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> sendPacketSync(String subChannel, Packet packet) {
        return Optional.of(ReformCloudAPIBungee.getInstance().sendPacketSync(subChannel, packet));
    }

    @Override
    public Optional<PacketFuture> createPacketFuture(Packet packet, String networkComponent) {
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().createPacketFuture(packet, networkComponent));
    }

    @Override
    public Optional<PacketFuture> sendPacketQuery(String channel, Packet packet) {
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().sendPacketQuery(channel, packet));
    }

    @Override
    public Optional<Client> getClient(String name) {
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().getClient(name));
    }

    @Override
    public Optional<ClientInfo> getConnectedClient(String name) {
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().getConnectedClient(name));
    }

    @Override
    public Optional<ServerInfo> getServerInfo(UUID uniqueID) {
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().getServerInfo(uniqueID));
    }

    @Override
    public Optional<ServerInfo> getServerInfo(String name) {
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().getServerInfo(name));
    }

    @Override
    public Optional<ProxyInfo> getProxyInfo(UUID uniqueID) {
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().getProxyInfo(uniqueID));
    }

    @Override
    public Optional<ProxyInfo> getProxyInfo(String name) {
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().getProxyInfo(name));
    }

    @Override
    public Optional<ServerGroup> getServerGroup(String name) {
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().getServerGroup(name));
    }

    @Override
    public Optional<ProxyGroup> getProxyGroup(String name) {
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().getProxyGroup(name));
    }

    @Override
    public Optional<NettyHandler> getNettyHandler() {
        return Optional.ofNullable(ReformCloudAPIBungee.getInstance().getNettyHandler());
    }
}
