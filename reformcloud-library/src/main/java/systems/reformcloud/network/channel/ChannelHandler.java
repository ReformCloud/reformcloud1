/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.channel;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.event.enums.EventTargetType;
import systems.reformcloud.event.events.OutGoingPacketEvent;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.utility.threading.TaskScheduler;

import java.util.*;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

public class ChannelHandler {
    private Map<String, ChannelHandlerContext> channelHandlerContextMap = ReformCloudLibraryService.concurrentHashMap();
    private TaskScheduler packetTask = new TaskScheduler(1);

    @Getter
    private Map<UUID, Packet> results = ReformCloudLibraryService.concurrentHashMap();

    /**
     * Get a specific {@link ChannelHandlerContext} by name
     *
     * @param name
     * @return a specific {@link ChannelHandlerContext} by given name
     * or null if the channel isn't registered
     */
    public ChannelHandlerContext getChannel(String name) {
        return this.channelHandlerContextMap.getOrDefault(name, null);
    }

    /**
     * Get if a specific channel is registered
     *
     * @param name
     * @return if the given ChannelName is registered
     */
    public boolean isChannelRegistered(final String name) {
        return this.channelHandlerContextMap.containsKey(name);
    }

    /**
     * Get a Set of all registered channels
     *
     * @return {@link Set<String>} of all registered channel names as {@link String}
     */
    public Set<String> getChannels() {
        return this.channelHandlerContextMap.keySet();
    }

    /**
     * Get all registered {@link ChannelHandlerContext}
     *
     * @return a List of all registered channels as {@link ChannelHandlerContext}
     */
    public List<ChannelHandlerContext> getChannelList() {
        return new ArrayList<>(this.channelHandlerContextMap.values());
    }

    /**
     * Register a channel by the given name
     *
     * @param name
     * @param channelHandlerContext
     */
    public void registerChannel(final String name, ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContextMap.put(name, channelHandlerContext);
    }

    /**
     * Unregisters a channel by the given name
     *
     * @param name
     */
    public void unregisterChannel(String name) {
        this.channelHandlerContextMap.remove(name);
    }

    /**
     * Removes all registered channels
     */
    public void clearChannels() {
        this.channelHandlerContextMap.clear();
    }

    /**
     * Gets the specific Handler name by the given {@link ChannelHandlerContext}
     *
     * @param channelHandlerContext
     * @return the specific Handler name by the given {@link ChannelHandlerContext}
     */
    public String getChannelNameByValue(final ChannelHandlerContext channelHandlerContext) {
        for (Map.Entry<String, ChannelHandlerContext> entry : this.channelHandlerContextMap.entrySet())
            if (channelHandlerContext.equals(entry.getValue()))
                return entry.getKey();

        return null;
    }

    /**
     * Closes a specific Channel by the name if the channel is registered
     *
     * @param name
     * @return {@link ChannelHandler}
     */
    public ChannelHandler closeChannel(final String name) {
        if (this.channelHandlerContextMap.containsKey(name))
            this.channelHandlerContextMap.get(name).channel().close();

        return this;
    }

    /**
     * Sends synchronised a {@link Packet} over the given Channel name as {@link String}
     *
     * @param channel
     * @param packet
     * @return if the channel is registered, to check if the packet was send
     */
    public boolean sendPacketSynchronized(final String channel, final Packet packet) {
        OutGoingPacketEvent outGoingPacketEvent = new OutGoingPacketEvent(false, packet);
        ReformCloudLibraryServiceProvider.getInstance().getEventManager().callEvent(EventTargetType.OUTGOING_PACKET, outGoingPacketEvent);

        if (outGoingPacketEvent.isCancelled())
            return false;

        if (this.channelHandlerContextMap.containsKey(channel))
            this.channelHandlerContextMap.get(channel).writeAndFlush(packet);

        return this.channelHandlerContextMap.containsKey(channel);
    }

    /**
     * Sends synchronised more than one {@link Packet} over the given Channel name as {@link String}
     *
     * @param channel
     * @param packets
     * @return if the channel is registered, to check if the packets were send
     */
    public boolean sendPacketSynchronized(final String channel, final Packet... packets) {
        if (this.channelHandlerContextMap.containsKey(channel))
            for (Packet packet : packets) {
                this.sendPacketSynchronized(channel, packet);
                ReformCloudLibraryService.sleep(10);
            }

        return this.channelHandlerContextMap.containsKey(channel);
    }

    /**
     * Sends asynchronous a {@link Packet} over the given Channel name as {@link String}
     *
     * @param channel
     * @param packet
     * @return if the channel is registered, to check if the packets were send
     */
    public boolean sendPacketAsynchronous(final String channel, final Packet packet) {
        OutGoingPacketEvent outGoingPacketEvent = new OutGoingPacketEvent(false, packet);
        ReformCloudLibraryServiceProvider.getInstance().getEventManager().callEvent(EventTargetType.OUTGOING_PACKET, outGoingPacketEvent);

        if (outGoingPacketEvent.isCancelled())
            return false;

        TaskScheduler.runtimeScheduler().schedule(() -> {
            if (this.channelHandlerContextMap.containsKey(channel))
                this.channelHandlerContextMap.get(channel).writeAndFlush(packet);
        });

        return this.channelHandlerContextMap.containsKey(channel);
    }

    /**
     * Sends asynchronous more than one {@link Packet} over the given Channel name as {@link String}
     *
     * @param channel
     * @param packets
     * @return if the channel is registered, to check if the packets were send
     */
    public boolean sendPacketAsynchronous(final String channel, final Packet... packets) {
        TaskScheduler.runtimeScheduler().schedule(() -> {
            if (this.channelHandlerContextMap.containsKey(channel))
                for (Packet packet : packets)
                    this.sendPacketAsynchronous(channel, packet);
        });

        return this.channelHandlerContextMap.containsKey(channel);
    }

    public boolean sendPacketQuery(final String channel, final String from, final Packet packet, final NetworkQueryInboundHandler handler) {
        ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().coloured("§5sending query");

        if (!this.channelHandlerContextMap.containsKey(channel))
            return false;

        ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().coloured("§5channel izz da");

        UUID result = UUID.randomUUID();
        packet.setResult(result);
        packet.getConfiguration().addStringProperty("from", from);
        results.put(result, new Packet().emptyPacket());

        packetTask.schedule(() -> {
            this.channelHandlerContextMap.get(channel).channel().writeAndFlush(packet);
            ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().coloured("§5sended in task");
        });

        int i = 0;

        while (results.get(result).getResult() == null) {
            i++;

            if (i >= 100000)
                break;

            try {
                Thread.sleep(0, 50000);
            } catch (final InterruptedException ignored) {
            }
        }

        ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().coloured("§5finished");
        handler.handle(this.results.get(result).getConfiguration(), result);
        return true;
    }

    /**
     * Sends synchronised a {@link Packet} to all registered Handlers
     *
     * @param packet
     */
    public void sendToAllSynchronized(Packet packet) {
        OutGoingPacketEvent outGoingPacketEvent = new OutGoingPacketEvent(false, packet);
        ReformCloudLibraryServiceProvider.getInstance().getEventManager().callEvent(EventTargetType.OUTGOING_PACKET, outGoingPacketEvent);

        if (outGoingPacketEvent.isCancelled())
            return;

        this.channelHandlerContextMap.values().forEach((consumer -> consumer.channel().writeAndFlush(packet)));
    }

    /**
     * Sends asynchronous a {@link Packet} to all registered Handlers
     *
     * @param packet
     */
    public void sendToAllAsynchronous(Packet packet) {
        OutGoingPacketEvent outGoingPacketEvent = new OutGoingPacketEvent(false, packet);
        ReformCloudLibraryServiceProvider.getInstance().getEventManager().callEvent(EventTargetType.OUTGOING_PACKET, outGoingPacketEvent);

        if (outGoingPacketEvent.isCancelled())
            return;

        TaskScheduler.runtimeScheduler().schedule(() ->
                this.channelHandlerContextMap.values().forEach((consumer -> consumer.channel().writeAndFlush(packet)))
        );
    }

    /**
     * Sends synchronised more than one {@link Packet} to all registered Handlers
     *
     * @param packets
     */
    public void sendToAllSynchronized(Packet... packets) {
        for (Packet packet : packets)
            this.sendToAllSynchronized(packet);
    }

    /**
     * Sends asynchronous more than one {@link Packet} to all registered Handlers
     *
     * @param packets
     */
    public void sendToAllAsynchronous(Packet... packets) {
        for (Packet packet : packets)
            this.sendToAllAsynchronous(packet);
    }
}
