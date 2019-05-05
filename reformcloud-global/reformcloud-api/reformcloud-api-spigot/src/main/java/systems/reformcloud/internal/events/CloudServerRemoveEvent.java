/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.internal.events;

import org.bukkit.event.HandlerList;
import systems.reformcloud.meta.info.ServerInfo;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

public final class CloudServerRemoveEvent extends DefaultCloudEvent implements Serializable {
    private static final HandlerList handlerList = new HandlerList();

    private ServerInfo serverInfo;

    @java.beans.ConstructorProperties({"serverInfo"})
    public CloudServerRemoveEvent(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public ServerInfo getServerInfo() {
        return this.serverInfo;
    }
}
