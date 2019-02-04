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
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

@AllArgsConstructor
@Getter
public class CloudServerRemoveEvent extends Event {
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
