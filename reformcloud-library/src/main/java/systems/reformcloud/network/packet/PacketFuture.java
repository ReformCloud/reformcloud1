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

    private String to;

    public PacketFuture(ChannelHandler channelHandler, Packet packet, ExecutorService executorService) {
        this.completableFuture = new CompletableFuture<>();
        this.executorService = executorService;
        this.channelHandler = channelHandler;
        this.sentPacket = packet;
    }

    public PacketFuture(ChannelHandler channelHandler, Packet packet, ExecutorService executorService, String to) {
        this.completableFuture = new CompletableFuture<>();
        this.executorService = executorService;
        this.channelHandler = channelHandler;
        this.sentPacket = packet;
        this.to = to;
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
            this.channelHandler.sendDirectPacket(to, sentPacket);

            Packet packet = this.syncUninterruptedly();
            if (packet.getResult() == null && this.onFailure != null)
                this.onFailure.handle(packet.getConfiguration(), packet.getResult());
            else if (this.onSuccess != null)
                this.onSuccess.handle(packet.getConfiguration(), packet.getResult());
        });
    }

    public PacketFuture sendOnCurrentThread() {
        if (this.to == null)
            return null;

        this.channelHandler.sendPacketSynchronized(this.to, sentPacket);
        return this;
    }

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

    public Packet syncUninterruptedly() {
        try {
            return this.completableFuture.get(10, TimeUnit.SECONDS);
        } catch (final InterruptedException | ExecutionException | TimeoutException ex) {
            return Packet.emptyPacket();
        }
    }

    public Packet syncUninterruptedly(long timeout, TimeUnit timeUnit) {
        try {
            return this.completableFuture.get(timeout, timeUnit);
        } catch (final InterruptedException | ExecutionException | TimeoutException ex) {
            return Packet.emptyPacket();
        }
    }

    public void handleIncoming(Packet in) {
        completableFuture.complete(in);
        channelHandler.getResults().remove(in.getResult());
    }
}
