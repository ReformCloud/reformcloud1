/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.internal.events;

import org.bukkit.event.HandlerList;
import systems.reformcloud.meta.info.ProxyInfo;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 12.03.2019
 */

public final class CloudProxyInfoUpdateEvent extends DefaultCloudEvent implements Serializable {

    private static final HandlerList handlerList = new HandlerList();

    private ProxyInfo proxyInfo;

    @java.beans.ConstructorProperties({"proxyInfo"})
    public CloudProxyInfoUpdateEvent(ProxyInfo proxyInfo) {
        this.proxyInfo = proxyInfo;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public ProxyInfo getProxyInfo() {
        return this.proxyInfo;
    }
}
