/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.update;

import systems.reformcloud.event.utility.Event;
import systems.reformcloud.meta.info.ProxyInfo;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 17.04.2019
 */

public final class ProxyInfoUpdateEvent extends Event implements Serializable {

    private ProxyInfo proxyInfo;

    @java.beans.ConstructorProperties({"proxyInfo"})
    public ProxyInfoUpdateEvent(ProxyInfo proxyInfo) {
        this.proxyInfo = proxyInfo;
    }

    public ProxyInfo getProxyInfo() {
        return this.proxyInfo;
    }
}
