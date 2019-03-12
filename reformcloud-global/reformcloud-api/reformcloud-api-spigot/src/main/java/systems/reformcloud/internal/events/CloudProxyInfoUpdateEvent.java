/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.internal.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import systems.reformcloud.meta.info.ProxyInfo;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 12.03.2019
 */

@AllArgsConstructor
@Getter
public final class CloudProxyInfoUpdateEvent extends Event implements Serializable {
    private static final HandlerList handlerList = new HandlerList();

    private ProxyInfo proxyInfo;

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
