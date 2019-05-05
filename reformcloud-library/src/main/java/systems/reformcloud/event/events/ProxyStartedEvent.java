/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events;

import systems.reformcloud.event.utility.Event;
import systems.reformcloud.meta.info.ProxyInfo;

import java.io.Serializable;

/**
 * The event will be called when a proxy process starts up
 *
 * @author _Klaro | Pasqual K. / created on 16.04.2019
 */

public final class ProxyStartedEvent extends Event implements Serializable {
    /**
     * The proxy info of the started process
     */
    private ProxyInfo proxyInfo;

    @java.beans.ConstructorProperties({"proxyInfo"})
    public ProxyStartedEvent(ProxyInfo proxyInfo) {
        this.proxyInfo = proxyInfo;
    }

    public ProxyInfo getProxyInfo() {
        return this.proxyInfo;
    }
}
