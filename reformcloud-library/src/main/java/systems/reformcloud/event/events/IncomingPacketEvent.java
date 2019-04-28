/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events;

import io.netty.channel.ChannelHandlerContext;
import systems.reformcloud.event.utility.Cancellable;
import systems.reformcloud.event.utility.Event;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * This event will be called if the cloud handles a packet
 *
 * @author _Klaro | Pasqual K. / created on 16.04.2019
 */

public final class IncomingPacketEvent extends Event implements Serializable, Cancellable {
    /**
     * The current cancel status
     */
    private boolean cancelled;

    /**
     * Sets the cancel status
     *
     * @param cancelled     The new cancel status
     */
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Get if the event is currently cancelled
     *
     * @return If the event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Creates a new event
     *
     * @param in                        The incoming packet
     * @param channelHandlerContext     The channel handler context of the channel the packet come from
     */
    public IncomingPacketEvent(Packet in, ChannelHandlerContext channelHandlerContext) {
        this.in = in;
        this.channelHandlerContext = channelHandlerContext;
    }

    /**
     * The packet which was handled
     */
    private Packet in;

    /**
     * The channel handler context of the channel the packet come from
     */
    private ChannelHandlerContext channelHandlerContext;

    public Packet getIn() {
        return this.in;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return this.channelHandlerContext;
    }
}
