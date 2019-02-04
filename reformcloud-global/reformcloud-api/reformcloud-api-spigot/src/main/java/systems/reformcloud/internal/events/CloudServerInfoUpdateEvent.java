/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.internal.events;

import systems.reformcloud.meta.info.ServerInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

@Getter
@AllArgsConstructor
public class CloudServerInfoUpdateEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();

    private ServerInfo serverInfo;

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
