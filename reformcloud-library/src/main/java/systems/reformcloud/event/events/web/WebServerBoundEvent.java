/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.web;

import systems.reformcloud.web.utils.WebHandlerAdapter;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public final class WebServerBoundEvent extends WebEvent implements Serializable {

    public WebServerBoundEvent(WebHandlerAdapter webHandlerAdapter, InetSocketAddress inetSocketAddress, long boundTime) {
        super(webHandlerAdapter);

        this.inetSocketAddress = inetSocketAddress;
        this.boundTime = boundTime;
    }

    private final InetSocketAddress inetSocketAddress;

    private final long boundTime;

    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }

    public long getBoudTime() {
        return boundTime;
    }
}
