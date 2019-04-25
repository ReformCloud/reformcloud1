/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events;

import io.netty.channel.ChannelHandlerContext;
import systems.reformcloud.event.utility.Event;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 19.04.2019
 */

public final class ChannelDisconnectedEvent extends Event implements Serializable {
    private ChannelHandlerContext channelHandlerContext;

    @java.beans.ConstructorProperties({"channelHandlerContext"})
    public ChannelDisconnectedEvent(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return this.channelHandlerContext;
    }
}
