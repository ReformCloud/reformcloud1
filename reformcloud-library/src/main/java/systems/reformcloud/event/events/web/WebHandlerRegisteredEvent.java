/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.web;

import systems.reformcloud.web.utils.WebHandler;
import systems.reformcloud.web.utils.WebHandlerAdapter;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public final class WebHandlerRegisteredEvent extends WebEvent implements Serializable {

    public WebHandlerRegisteredEvent(WebHandlerAdapter webHandlerAdapter, String path, WebHandler webHandler) {
        super(webHandlerAdapter);
        this.webHandler = webHandler;
        this.path = path;
    }

    private final WebHandler webHandler;

    private final String path;

    public WebHandler getWebHandler() {
        return webHandler;
    }

    public String getPath() {
        return path;
    }
}
