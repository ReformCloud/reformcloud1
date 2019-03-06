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
    private CompletableFuture<Packet> completableFuture;

    private final ExecutorService executorService;

    private NetworkQueryInboundHandler onSuccess, onFailure;

    private ChannelHandler channelHandler;
    private Packet sentPacket;

    public PacketFuture(ChannelHandler channelHandler, Packet packet, ExecutorService executorService) {
        this.completableFuture = new CompletableFuture<>();
        this.executorService = executorService;
        this.channelHandler = channelHandler;
        this.sentPacket = packet;
    }

    public PacketFuture onSuccessfullyCompleted(final NetworkQueryInboundHandler inboundHandler) {
        this.onSuccess = inboundHandler;
        return this;
    }

    public PacketFuture onFailure(final NetworkQueryInboundHandler inboundHandler) {
        this.onFailure = inboundHandler;
        return this;
    }

    public PacketFuture whenCompleted(final NetworkQueryInboundHandler onSuccess, final NetworkQueryInboundHandler onFailure) {
        this.onSuccess = onSuccess;
        this.onFailure = onFailure;
        return this;
    }

    public void send(String to) {
        this.executorService.execute(() -> {
            this.channelHandler.sendPacketSynchronized(to, sentPacket);

            Packet packet = this.syncUninterruptedly();
            if (this.onSuccess != null && this.onFailure != null) {
                if (packet.getResult() == null)
                    this.onFailure.handle(packet.getConfiguration(), packet.getResult());
                else
                    this.onSuccess.handle(packet.getConfiguration(), packet.getResult());
            }
        });
    }

    public void send(String to, long timeout, TimeUnit timeUnit) {
        executorService.execute(() -> {
            this.channelHandler.sendPacketSynchronized(to, sentPacket);

            Packet packet = this.syncUninterruptedly(timeout, timeUnit);
            if (this.onSuccess != null && this.onFailure != null) {
                if (packet.getResult() == null)
                    this.onFailure.handle(packet.getConfiguration(), packet.getResult());
                else
                    this.onSuccess.handle(packet.getConfiguration(), packet.getResult());
            }
        });
    }

    private Packet syncUninterruptedly() {
        try {
            return this.completableFuture.get(20, TimeUnit.SECONDS);
        } catch (final InterruptedException | ExecutionException | TimeoutException ex) {
            return Packet.emptyPacket();
        }
    }

    private Packet syncUninterruptedly(long timeout, TimeUnit timeUnit) {
        try {
            return this.completableFuture.get(timeout, timeUnit);
        } catch (final InterruptedException | ExecutionException | TimeoutException ex) {
            return Packet.emptyPacket();
        }
    }

    public void handleIncoming(Packet in) {
        completableFuture.complete(in);
    }
}
