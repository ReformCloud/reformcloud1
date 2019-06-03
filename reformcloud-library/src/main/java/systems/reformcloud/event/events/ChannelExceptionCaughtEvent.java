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

public final class ChannelExceptionCaughtEvent extends Event implements Serializable {

    private ChannelHandlerContext channelHandlerContext;
    private Throwable cause;

    @java.beans.ConstructorProperties({"channelHandlerContext", "cause"})
    public ChannelExceptionCaughtEvent(ChannelHandlerContext channelHandlerContext,
        Throwable cause) {
        this.channelHandlerContext = channelHandlerContext;
        this.cause = cause;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return this.channelHandlerContext;
    }

    public Throwable getCause() {
        return this.cause;
    }
}
