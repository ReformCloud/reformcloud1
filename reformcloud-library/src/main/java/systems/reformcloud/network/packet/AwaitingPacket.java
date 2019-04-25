/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packet;

import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 27.03.2019
 */

public final class AwaitingPacket implements Serializable {
    /**
     * The channel where the packet should be sent through
     */
    private ChannelHandlerContext channelHandlerContext;

    /**
     * The packet which should be sent
     */
    private Packet packet;

    @java.beans.ConstructorProperties({"channelHandlerContext", "packet"})
    public AwaitingPacket(ChannelHandlerContext channelHandlerContext, Packet packet) {
        this.channelHandlerContext = channelHandlerContext;
        this.packet = packet;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return this.channelHandlerContext;
    }

    public Packet getPacket() {
        return this.packet;
    }
}
