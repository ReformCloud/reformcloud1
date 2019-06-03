/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.listeners.proxy;

import java.io.Serializable;
import systems.reformcloud.CloudFlareUtil;
import systems.reformcloud.event.annotation.Handler;
import systems.reformcloud.event.events.ProxyStartedEvent;
import systems.reformcloud.event.utility.Listener;

/**
 * @author _Klaro | Pasqual K. / created on 10.05.2019
 */

public final class ProxyStartedListener implements Serializable, Listener {

    @Handler
    public void handleStart(final ProxyStartedEvent event) {
        CloudFlareUtil.getInstance().createProxyEntry(event.getProxyInfo());
    }
}
