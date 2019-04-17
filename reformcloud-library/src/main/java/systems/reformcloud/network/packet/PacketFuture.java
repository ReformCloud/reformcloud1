/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packet;

import lombok.Getter;
import systems.reformcloud.network.channel.ChannelHandler;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;

import java.io.Serializable;
import java.util.concurrent.*;

/**
 * @author _Klaro | Pasqual K. / created on 06.03.2019
 */

@Getter
public final class PacketFuture implements Serializable {
    /**
     * The completable future for the packets
     */
    private CompletableFuture<Packet> completableFuture;

    /**
     * The executor service used to run a lot of task at the same time
     */
    private final ExecutorService executorService;

    /**
     * The network handlers for the result states of the query
     */
    private NetworkQueryInboundHandler onSuccess, onFailure;

    /**
     * The channel handler used to identify the query packets
     */
    private ChannelHandler channelHandler;

    /**
     * The sent packet to the other network participant
     */
    private Packet sentPacket;

    /**
     * The name of the other network participant
     */
    private String to;

    /**
     * Creates a new packet future
     *
     * @param channelHandler  The channel handler which should be used to identify the packets
     * @param packet          The packet which should be sent
     * @param executorService The executor service which should be used to send the packets
     */
    public PacketFuture(ChannelHandler channelHandler, Packet packet, ExecutorService executorService) {
        this.completableFuture = new CompletableFuture<>();
        this.executorService = executorService;
        this.channelHandler = channelHandler;
        this.sentPacket = packet;
    }

    /**
     * Creates a new packet future
     *
     * @param channelHandler        The channel handler which should be used to identify the packets
     * @param packet                The packet which should be sent
     * @param executorService       The executor service which should be used to send the packets
     * @param to                    The name of the network participant who should receive the packet
     */
    public PacketFuture(ChannelHandler channelHandler, Packet packet, ExecutorService executorService, String to) {
        this.completableFuture = new CompletableFuture<>();
        this.executorService = executorService;
        this.channelHandler = channelHandler;
        this.sentPacket = packet;
        this.to = to;
    }

    /**
     * This method will set the successful network inbound handler
     *
     * @param inboundHandler        The new successful network handler
     * @return The current instance of this class
     */
    public PacketFuture onSuccessfullyCompleted(final NetworkQueryInboundHandler inboundHandler) {
        this.onSuccess = inboundHandler;
        return this;
    }

    /**
     * This method will set the failure network inbound handler
     *
     * @param inboundHandler        The new failure network handler
     * @return The current instance of this class
     */
    public PacketFuture onFailure(final NetworkQueryInboundHandler inboundHandler) {
        this.onFailure = inboundHandler;
        return this;
    }

    /**
     * This method will set the failure and the successful packet handler
     *
     * @param onSuccess         The new successful network handler
     * @param onFailure         The new failure network handler
     * @return The current instance of this class
     */
    public PacketFuture whenCompleted(final NetworkQueryInboundHandler onSuccess, final NetworkQueryInboundHandler onFailure) {
        return this.onSuccessfullyCompleted(onSuccess).onFailure(onFailure);
    }

    /**
     * Sends the packet to the network participant on a new thread
     *
     * @param to    The network participant name
     */
    public void send(String to) {
        this.executorService.execute(() -> {
            this.channelHandler.sendDirectPacket(to, sentPacket);

            Packet packet = this.syncUninterruptedly();
            if (packet.getResult() == null && this.onFailure != null)
                this.onFailure.handle(packet.getConfiguration(), packet.getResult());
            else if (this.onSuccess != null)
                this.onSuccess.handle(packet.getConfiguration(), packet.getResult());
        });
    }

    /**
     * Sends the packet on the current thread
     *
     * @return The current instance of this class
     */
    public PacketFuture sendOnCurrentThread() {
        if (this.to == null)
            return null;

        this.channelHandler.sendPacketSynchronized(this.to, sentPacket);
        return this;
    }

    /**
     * Sends the packet to the network participant on a new thread
     *
     * @param to            The network participant name
     * @param timeout       The time how long the cloud should wait for a response
     * @param timeUnit      The time unit in which the cloud should wait for a response
     */
    public void send(String to, long timeout, TimeUnit timeUnit) {
        executorService.execute(() -> {
            this.channelHandler.sendDirectPacket(to, sentPacket);

            Packet packet = this.syncUninterruptedly(timeout, timeUnit);
            if (packet.getResult() == null && this.onFailure != null)
                this.onFailure.handle(packet.getConfiguration(), packet.getResult());
            else if (this.onSuccess != null)
                this.onSuccess.handle(packet.getConfiguration(), packet.getResult());
        });
    }

    /**
     * Waits on the current thread 10 seconds for a response
     *
     * @return The response packet or an empty packet
     */
    public Packet syncUninterruptedly() {
        try {
            return this.completableFuture.get(10, TimeUnit.SECONDS);
        } catch (final InterruptedException | ExecutionException | TimeoutException ex) {
            return Packet.EMPTY_PACKET;
        }
    }

    /**
     * Waits on the current thread the given time for a response
     *
     * @param timeout       The time how long the cloud should wait
     * @param timeUnit      The time unit in which the cloud should wait
     * @return The response packet or an empty packet
     */
    public Packet syncUninterruptedly(long timeout, TimeUnit timeUnit) {
        try {
            return this.completableFuture.get(timeout, timeUnit);
        } catch (final InterruptedException | ExecutionException | TimeoutException ex) {
            return Packet.EMPTY_PACKET;
        }
    }

    /**
     * Handel an incoming response
     *
     * @param in    The packet which was sent by the other network participant
     */
    public void handleIncoming(Packet in) {
        completableFuture.complete(in);
        channelHandler.getResults().remove(in.getResult());
    }
}
