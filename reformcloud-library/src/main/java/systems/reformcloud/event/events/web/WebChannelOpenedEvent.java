/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.web;

import io.netty.channel.Channel;
import systems.reformcloud.web.utils.WebHandlerAdapter;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public final class WebChannelOpenedEvent extends WebEvent implements Serializable {

    public WebChannelOpenedEvent(WebHandlerAdapter webHandlerAdapter, Channel channel) {
        super(webHandlerAdapter);
        this.channel = channel;
    }

    private final Channel channel;

    public Channel getChannel() {
        return channel;
    }
}
