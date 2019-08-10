/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.web;

import io.netty.channel.ChannelFuture;
import systems.reformcloud.web.utils.WebHandlerAdapter;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public final class WebChannelClosedEvent extends WebEvent implements Serializable {

    public WebChannelClosedEvent(WebHandlerAdapter webHandlerAdapter, ChannelFuture channelFuture) {
        super(webHandlerAdapter);
        this.channelFuture = channelFuture;
    }

    private final ChannelFuture channelFuture;

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }
}
