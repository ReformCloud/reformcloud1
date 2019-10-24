/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.web;

import io.netty.handler.codec.http.HttpRequest;
import systems.reformcloud.event.utility.Cancellable;
import systems.reformcloud.web.utils.WebHandler;
import systems.reformcloud.web.utils.WebHandlerAdapter;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public final class WebHandlerPreHandleEvent extends WebEvent implements Serializable, Cancellable {

    public WebHandlerPreHandleEvent(WebHandlerAdapter webHandlerAdapter, WebHandler webHandler, HttpRequest httpRequest) {
        super(webHandlerAdapter);

        this.webHandler = webHandler;
        this.httpRequest = httpRequest;
    }

    private final WebHandler webHandler;

    private final HttpRequest httpRequest;

    public WebHandler getWebHandler() {
        return webHandler;
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    private boolean cancelled;

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
