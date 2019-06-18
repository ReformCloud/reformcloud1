/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.channel;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.event.events.OutgoingPacketEvent;
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
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

public final class ChannelHandler extends AbstractChannelHandler implements Serializable {

    /**
     * All registered channels of the cloud system
     */
    private Map<String, ChannelHandlerContext> channelHandlerContextMap = ReformCloudLibraryService
        .concurrentHashMap();

    /**
     * All waiting query packet
     */
    private Map<UUID, PacketFuture> results = ReformCloudLibraryService.concurrentHashMap();

    /**
     * The executor service to run several tasks at the same time
     */
    private final ExecutorService executorService = ReformCloudLibraryService.EXECUTOR_SERVICE;

    /**
     * Creates a new instance of the channel handler
     */
    public ChannelHandler() {
        ReformCloudLibraryServiceProvider.getInstance().setChannelHandler(this);
    }

    @Override
    public NetworkGlobalCluster shiftClusterNetworkInformation() {
        List<ClusterChannelInformation> information = new ArrayList<>();
        return new NetworkGlobalCluster(
            this.channelHandlerContextMap.keySet(),
            information,
            this,
            ReformCloudLibraryServiceProvider.getInstance().getInternalCloudNetwork()
        );
    }

    /**
     * Gets a specific channel handler context by the name of the network participant
     *
     * @param name The name of the network participant
     * @return The channel handler context or {@code null} if the channel can't be found
     */
    @Override
    public ChannelHandlerContext getChannel(String name) {
        return this.channelHandlerContextMap.getOrDefault(name, null);
    }

    /**
     * Checks if a specific channel is registered
     *
     * @param name The name of the network participant
     * @return If the channel is registered in the cloud system
     */
    @Override
    public boolean isChannelRegistered(final String name) {
        return this.channelHandlerContextMap.containsKey(name);
    }

    /**
     * Checks if a specific channel is registered
     *
     * @param channelHandlerContext The channel handler context of the network participant
     * @return If the channel is registered in the cloud system
     */
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

    /**
     * Get a list of all channel registered in the cloud system
     *
     * @return A set containing all registered channels
     */
    @Override
    public Set<String> getChannels() {
        return this.channelHandlerContextMap.keySet();
    }

    /**
     * Get a list of all registered channels handler contexts
     *
     * @return A list containing all registered channels handler contexts
     */
    @Override
    public List<ChannelHandlerContext> getChannelList() {
        return new ArrayList<>(this.channelHandlerContextMap.values());
    }

    /**
     * Register a channel by the given name
     *
     * @param name The name of the network participant
     * @param channelHandlerContext The channel handler context of the network participant
     */
    @Override
    public void registerChannel(final String name, ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContextMap.put(name, channelHandlerContext);
    }

    /**
     * Unregisters a channel by the given name
     *
     * @param name The name of the network participant
     */
    @Override
    public void unregisterChannel(String name) {
        this.channelHandlerContextMap.remove(name);
    }

    /**
     * Unregisters all registered channels
     */
    @Override
    public void clearChannels() {
        this.channelHandlerContextMap.clear();
    }

    /**
     * Get a specific network participant name
     *
     * @param channelHandlerContext The channel handler context of the network participant
     * @return The specific network participant name
     */
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

    /**
     * Sends a packet into a channel
     *
     * @param channel The network participant name
     * @param packet The packet which should be sent
     * @return If the cloud could send the packet into the channel
     */
    @Override
    public boolean sendPacketSynchronized(final String channel, final Packet packet) {
        if (this.channelHandlerContextMap.containsKey(channel)) {
            this.sendPacket0(packet, this.channelHandlerContextMap.get(channel));
        }

        return this.channelHandlerContextMap.containsKey(channel);
    }

    /**
     * Sends a direct packet to a network participant
     *
     * @param to The name of the network participant
     * @param packet The packet which should be sent
     */
    @Override
    public void sendDirectPacket(String to, Packet packet) {
        if (!this.channelHandlerContextMap.containsKey(to)) {
            return;
        }

        this.sendPacket0(packet, this.channelHandlerContextMap.get(to));
    }

    /**
     * Sends a packet into a channel
     *
     * @param channel The network participant name
     * @param packets The packets which should be sent
     * @return If the cloud could send the packet into the channel
     */
    @Override
    public boolean sendPacketSynchronized(final String channel, final Packet... packets) {
        if (this.channelHandlerContextMap.containsKey(channel)) {
            for (Packet packet : packets) {
                this.sendPacketSynchronized(channel, packet);
            }
        }

        return this.channelHandlerContextMap.containsKey(channel);
    }

    /**
     * Sends a packet into a channel
     *
     * @param channel The network participant name
     * @param packet The packet which should be sent
     * @return If the cloud could send the packet into the channel
     */
    @Override
    public boolean sendPacketAsynchronous(final String channel, final Packet packet) {
        CompletableFuture.runAsync(() -> {
            if (this.channelHandlerContextMap.containsKey(channel)) {
                this.sendPacket0(packet, this.channelHandlerContextMap.get(channel));
            }
        });

        return this.channelHandlerContextMap.containsKey(channel);
    }

    /**
     * Sends a packet into a channel
     *
     * @param channel The network participant name
     * @param packets The packets which should be sent
     * @return If the cloud could send the packet into the channel
     */
    @Override
    public boolean sendPacketAsynchronous(final String channel, final Packet... packets) {
        if (this.channelHandlerContextMap.containsKey(channel)) {
            for (Packet packet : packets) {
                this.sendPacketAsynchronous(channel, packet);
            }
        }

        return this.channelHandlerContextMap.containsKey(channel);
    }

    /**
     * Sends a packet query
     *
     * @param channel The network participant name
     * @param from The current handler name
     * @param packet The packet which should be sent
     * @param handler The handler which should be called if the query succeeds
     * @param onFailure The handler which should be called if the query fails
     * @return If the creation of the future was successful
     */
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

    /**
     * Sends a packet query
     *
     * @param channel The network participant name
     * @param from The current handler name
     * @param packet The packet which should be sent
     * @param handler The handler which should be called if the query succeeds
     * @return If the creation of the future was successful
     */
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

    /**
     * Sends a packet query
     *
     * @param channel The network participant name
     * @param from The current handler name
     * @param packet The packet which should be sent
     * @return The created packet future
     */
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

    /**
     * Sends a packet to all channels
     *
     * @param packet The packet which should be sent
     */
    @Override
    public void sendToAllSynchronized(Packet packet) {
        this.channelHandlerContextMap.values()
            .forEach(consumer -> this.sendPacket0(packet, consumer));
    }

    /**
     * Sends a packet to all channels
     *
     * @param packet The packet which should be sent
     */
    @Override
    public void sendToAllAsynchronous(Packet packet) {
        CompletableFuture.runAsync(() -> this.channelHandlerContextMap.values()
            .forEach(consumer -> this.sendPacket0(packet, consumer)));
    }

    /**
     * Sends a packet to all channels
     *
     * @param packet The packet which should be sent
     */
    @Override
    public void sendToAllDirect(Packet packet) {
        this.channelHandlerContextMap.forEach((key, value) -> this.sendDirectPacket(key, packet));
    }

    /**
     * Sends a packet to all channels
     *
     * @param packets The packets which should be sent
     */
    @Override
    public void sendToAllSynchronized(Packet... packets) {
        for (Packet packet : packets) {
            this.sendToAllSynchronized(packet);
        }
    }

    /**
     * Sends a packet to all channels
     *
     * @param packets The packets which should be sent
     */
    @Override
    public void sendToAllAsynchronous(Packet... packets) {
        for (Packet packet : packets) {
            this.sendToAllAsynchronous(packet);
        }
    }

    /**
     * Sends a packet to all lobby servers
     *
     * @param provider The server process manager which should be used to identify the lobbies
     * @param packets The packets which should be send
     */
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

    /**
     * Sends a direct packet to all lobby servers
     *
     * @param provider The server process manager which should be used to identify the lobbies
     * @param packets The packets which should be send
     */
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

    /**
     * Converts a normal packet into a query packet
     *
     * @param packet The packet which should be converted
     * @param resultID The result id which should be set
     * @param component The current component name
     */
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
