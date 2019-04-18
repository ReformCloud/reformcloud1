/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.internal.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import systems.reformcloud.meta.info.ProxyInfo;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

@AllArgsConstructor
@Getter
public final class CloudProxyAddEvent extends Event implements Serializable {
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
