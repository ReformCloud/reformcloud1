/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.internal.events;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import org.bukkit.event.HandlerList;
import systems.reformcloud.meta.info.ServerInfo;

/**
 * @author _Klaro | Pasqual K. / created on 29.06.2019
 */

public final class ServerInfoPreUpdateEvent extends DefaultCloudEvent implements Serializable {

    private final HandlerList handlerList = new HandlerList();

    private ServerInfo oldServerInfo;

    private ServerInfo newServerInfo;

    @ConstructorProperties({"oldServerInfo", "newServerInfo"})
    public ServerInfoPreUpdateEvent(ServerInfo oldServerInfo,
        ServerInfo newServerInfo) {
        this.oldServerInfo = oldServerInfo;
        this.newServerInfo = newServerInfo;
    }

    public HandlerList getHandlerList() {
        return handlerList;
    }

    public ServerInfo getOldServerInfo() {
        return oldServerInfo;
    }

    public ServerInfo getNewServerInfo() {
        return newServerInfo;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
