/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.events;

import systems.reformcloud.meta.info.ProxyInfo;

import java.io.Serializable;

/**
 * This class represents the update of proxy info update event in the cloud system
 *
 * @author _Klaro | Pasqual K. / created on 12.03.2019
 */

public final class CloudProxyInfoUpdateEvent implements Serializable {

    private ProxyInfo proxyInfo;

    @java.beans.ConstructorProperties({"proxyInfo"})
    public CloudProxyInfoUpdateEvent(ProxyInfo proxyInfo) {
        this.proxyInfo = proxyInfo;
    }

    public ProxyInfo getProxyInfo() {
        return this.proxyInfo;
    }
}
