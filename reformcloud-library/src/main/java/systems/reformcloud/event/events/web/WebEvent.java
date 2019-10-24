/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.web;

import systems.reformcloud.event.utility.Event;
import systems.reformcloud.web.utils.WebHandlerAdapter;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public abstract class WebEvent extends Event implements Serializable {

    public WebEvent(WebHandlerAdapter webHandlerAdapter) {
        this.webHandlerAdapter = webHandlerAdapter;
    }

    private final WebHandlerAdapter webHandlerAdapter;

    public WebHandlerAdapter getWebHandlerAdapter() {
        return webHandlerAdapter;
    }
}
