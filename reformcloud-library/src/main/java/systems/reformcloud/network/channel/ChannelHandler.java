/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.channel;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.event.events.network.OutgoingPacketEvent;
import systems.reformcloud.meta.cluster.NetworkGlobalCluster;
import systems.reformcloud.meta.cluster.channel.ClusterChannelInformation;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.abstracts.AbstractChannelHandler;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.network.packet.PacketFuture;
import systems.reformcloud.utility.cloudsystem.ServerProcessManager;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

public final class ChannelHandler extends AbstractChannelHandler implements Serializable {

    private Map<String, ChannelHandlerContext> channelHandlerContextMap = ReformCloudLibraryService
        .concurrentHashMap();

    private Map<UUID, PacketFuture> results = ReformCloudLibraryService.concurrentHashMap();

    private final ExecutorService executorService = ReformCloudLibraryService.EXECUTOR_SERVICE;

    public ChannelHandler() {
        ReformCloudLibraryServiceProvider.getInstance().setChannelHandler(this);
    }

    @Override
    public NetworkGlobalCluster shiftClusterNetworkInformation() {
        List<ClusterChannelInformation> information = new ArrayList<>();
        channelHandlerContextMap.forEach((k, v) -> {
            ClusterChannelInformation clusterChannelInformation = new ClusterChannelInformation(
                k, v, ReformCloudLibraryService.apply(v.channel().remoteAddress(), socketAddress -> (InetSocketAddress) socketAddress)
            );
            information.add(clusterChannelInformation);
        });
        return new NetworkGlobalCluster(
            this.channelHandlerContextMap.keySet(),
            information,
            this,
            ReformCloudLibraryServiceProvider.getInstance().getInternalCloudNetwork()
        );
    }

    @Override
    public ChannelHandlerContext getChannel(String name) {
        return this.channelHandlerContextMap.getOrDefault(name, null);
    }

    @Override
    public boolean isChannelRegistered(final String name) {
        return this.channelHandlerContextMap.containsKey(name);
    }

    @Override
    public boolean isChannelRegistered(ChannelHandlerContext channelHandlerContext) {
        for (Map.Entry<String, ChannelHandlerContext> map : this.channelHandlerContextMap
            .entrySet()) {
            if (map.getValue().equals(channelHandlerContext)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Set<String> getChannels() {
        return this.channelHandlerContextMap.keySet();
    }

    @Override
    public List<ChannelHandlerContext> getChannelList() {
        return new ArrayList<>(this.channelHandlerContextMap.values());
    }

    @Override
    public void registerChannel(final String name, ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContextMap.put(name, channelHandlerContext);
    }

    @Override
    public void unregisterChannel(String name) {
        this.channelHandlerContextMap.remove(name);
    }

    @Override
    public void clearChannels() {
        this.channelHandlerContextMap.clear();
    }

    @Override
    public String getChannelNameByValue(final ChannelHandlerContext channelHandlerContext) {
        for (Map.Entry<String, ChannelHandlerContext> entry : this.channelHandlerContextMap
            .entrySet()) {
            if (channelHandlerContext.equals(entry.getValue())) {
                return entry.getKey();
            }
        }

        return null;
    }

    private void sendPacket0(Packet packet, ChannelHandlerContext channelHandlerContext) {
        if (packet == null || channelHandlerContext == null) {
            return;
        }

        OutgoingPacketEvent outgoingPacketEvent = new OutgoingPacketEvent(packet,
            channelHandlerContext);
        ReformCloudLibraryServiceProvider.getInstance().getEventManager().fire(outgoingPacketEvent);
        if (outgoingPacketEvent.isCancelled()) {
            return;
        }

        ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider()
            .debug("Sending packet [Type=" + packet.getType() + "/Message=" + packet.getConfiguration().getJsonString()
                + "/Size=" + packet.getConfiguration().getJsonString().getBytes().length
                + "/To=" + this.getChannelNameByValue(channelHandlerContext) + "]");

        if (channelHandlerContext.channel().eventLoop().inEventLoop()) {
            channelHandlerContext.channel()
                .writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        } else {
            channelHandlerContext.channel().eventLoop()
                .execute(() -> channelHandlerContext.channel()
                    .writeAndFlush(packet)
                    .addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE));
        }
    }

    @Override
    public boolean sendPacketSynchronized(final String channel, final Packet packet) {
        if (this.channelHandlerContextMap.containsKey(channel)) {
            this.sendPacket0(packet, this.channelHandlerContextMap.get(channel));
        }

        return this.channelHandlerContextMap.containsKey(channel);
    }

    @Override
    public void sendDirectPacket(String to, Packet packet) {
        if (!this.channelHandlerContextMap.containsKey(to)) {
            return;
        }

        this.sendPacket0(packet, this.channelHandlerContextMap.get(to));
    }

    @Override
    public boolean sendPacketSynchronized(final String channel, final Packet... packets) {
        if (this.channelHandlerContextMap.containsKey(channel)) {
            for (Packet packet : packets) {
                this.sendPacketSynchronized(channel, packet);
            }
        }

        return this.channelHandlerContextMap.containsKey(channel);
    }

    @Override
    public boolean sendPacketAsynchronous(final String channel, final Packet packet) {
        CompletableFuture.runAsync(() -> {
            if (this.channelHandlerContextMap.containsKey(channel)) {
                this.sendPacket0(packet, this.channelHandlerContextMap.get(channel));
            }
        });

        return this.channelHandlerContextMap.containsKey(channel);
    }

    @Override
    public boolean sendPacketAsynchronous(final String channel, final Packet... packets) {
        if (this.channelHandlerContextMap.containsKey(channel)) {
            for (Packet packet : packets) {
                this.sendPacketAsynchronous(channel, packet);
            }
        }

        return this.channelHandlerContextMap.containsKey(channel);
    }

    @Override
    public boolean sendPacketQuerySync(final String channel, final String from, final Packet packet,
                                       final NetworkQueryInboundHandler handler,
                                       final NetworkQueryInboundHandler onFailure) {
        if (!this.channelHandlerContextMap.containsKey(channel)) {
            return false;
        }

        UUID result = UUID.randomUUID();
        this.toQueryPacket(packet, result, from);

        PacketFuture packetFuture = new PacketFuture(this, packet, this.executorService);
        packetFuture.whenCompleted(handler, onFailure);

        this.results.put(result, packetFuture);
        packetFuture.send(channel);

        return true;
    }

    @Override
    public boolean sendPacketQuerySync(final String channel, final String from, final Packet packet,
                                       final NetworkQueryInboundHandler handler) {
        if (!this.channelHandlerContextMap.containsKey(channel)) {
            return false;
        }

        UUID result = UUID.randomUUID();
        this.toQueryPacket(packet, result, from);

        PacketFuture packetFuture = new PacketFuture(this, packet, this.executorService);
        packetFuture.onSuccessfullyCompleted(handler);

        this.results.put(result, packetFuture);
        packetFuture.send(channel);

        return true;
    }

    @Override
    public PacketFuture sendPacketQuerySync(final String channel, final String from,
                                            final Packet packet) {
        if (!this.channelHandlerContextMap.containsKey(channel)) {
            return null;
        }

        UUID result = UUID.randomUUID();
        this.toQueryPacket(packet, result, from);

        PacketFuture packetFuture = new PacketFuture(this, packet, this.executorService, channel);
        this.results.put(result, packetFuture);

        return packetFuture;
    }

    @Override
    public void sendToAllSynchronized(Packet packet) {
        this.channelHandlerContextMap.values()
            .forEach(consumer -> this.sendPacket0(packet, consumer));
    }

    @Override
    public void sendToAllAsynchronous(Packet packet) {
        CompletableFuture.runAsync(() -> this.channelHandlerContextMap.values()
            .forEach(consumer -> this.sendPacket0(packet, consumer)));
    }

    @Override
    public void sendToAllDirect(Packet packet) {
        this.channelHandlerContextMap.forEach((key, value) -> this.sendDirectPacket(key, packet));
    }

    @Override
    public void sendToAllSynchronized(Packet... packets) {
        for (Packet packet : packets) {
            this.sendToAllSynchronized(packet);
        }
    }

    @Override
    public void sendToAllAsynchronous(Packet... packets) {
        for (Packet packet : packets) {
            this.sendToAllAsynchronous(packet);
        }
    }

    @Override
    public void sendToAllLobbies(ServerProcessManager provider, Packet... packets) {
        List<ServerInfo> lobbies = provider
            .getAllRegisteredServerProcesses()
            .stream()
            .filter(e -> e.getServerGroup().getServerModeType().equals(ServerModeType.LOBBY))
            .collect(Collectors.toList());
        for (ServerInfo serverInfo : lobbies) {
            for (Packet packet : packets) {
                this.sendPacketSynchronized(serverInfo.getCloudProcess().getName(), packet);
            }
        }
    }

    @Override
    public void sendToAllLobbiesDirect(ServerProcessManager provider, Packet... packets) {
        List<ServerInfo> lobbies = provider
            .getAllRegisteredServerProcesses()
            .stream()
            .filter(e -> e.getServerGroup().getServerModeType().equals(ServerModeType.LOBBY))
            .collect(Collectors.toList());
        for (ServerInfo serverInfo : lobbies) {
            for (Packet packet : packets) {
                this.sendDirectPacket(serverInfo.getCloudProcess().getName(), packet);
            }
        }
    }

    @Override
    public void toQueryPacket(Packet packet, UUID resultID, String component) {
        packet.setResult(resultID);
        packet.getConfiguration().addStringValue("from", component);
    }

    @Override
    public Map<UUID, PacketFuture> getResults() {
        return this.results;
    }

    @Override
    public ExecutorService getExecutorService() {
        return this.executorService;
    }
}
