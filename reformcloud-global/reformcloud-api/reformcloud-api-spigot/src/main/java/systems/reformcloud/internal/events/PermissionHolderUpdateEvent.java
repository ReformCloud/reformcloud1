/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.internal.events;

import org.bukkit.event.HandlerList;
import systems.reformcloud.player.permissions.player.PermissionHolder;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 05.05.2019
 */

public final class PermissionHolderUpdateEvent extends DefaultCloudEvent implements Serializable {

    private static final HandlerList handlerList = new HandlerList();
    private PermissionHolder permissionHolder;

    @java.beans.ConstructorProperties({"permissionHolder"})
    public PermissionHolderUpdateEvent(PermissionHolder permissionHolder) {
        this.permissionHolder = permissionHolder;
    }

    public PermissionHolder getPermissionHolder() {
        return permissionHolder;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
