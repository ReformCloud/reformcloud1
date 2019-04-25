/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.events;

import systems.reformcloud.meta.info.ProxyInfo;

/**
 * This class represents the remove of a cloud proxy
 *
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

public final class CloudProxyRemoveEvent {
    private ProxyInfo proxyInfo;

    @java.beans.ConstructorProperties({"proxyInfo"})
    public CloudProxyRemoveEvent(ProxyInfo proxyInfo) {
        this.proxyInfo = proxyInfo;
    }

    public ProxyInfo getProxyInfo() {
        return this.proxyInfo;
    }
}
