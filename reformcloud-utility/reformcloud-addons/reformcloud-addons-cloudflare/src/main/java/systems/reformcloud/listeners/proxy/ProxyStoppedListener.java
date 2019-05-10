/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.listeners.proxy;

import systems.reformcloud.CloudFlareUtil;
import systems.reformcloud.event.annotation.Handler;
import systems.reformcloud.event.events.ProxyStoppedEvent;
import systems.reformcloud.event.utility.Listener;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.05.2019
 */

public final class ProxyStoppedListener implements Serializable, Listener {
    @Handler
    public void handleStop(final ProxyStoppedEvent event) {
        CloudFlareUtil.getInstance().deleteProxyEntry(event.getProxyInfo());
    }
}
