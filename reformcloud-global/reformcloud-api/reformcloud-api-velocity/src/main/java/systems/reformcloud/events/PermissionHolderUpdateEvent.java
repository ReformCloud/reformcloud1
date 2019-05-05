/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.events;

import systems.reformcloud.player.permissions.player.PermissionHolder;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 05.05.2019
 */

public final class PermissionHolderUpdateEvent implements Serializable {
    private PermissionHolder permissionHolder;

    @java.beans.ConstructorProperties({"permissionHolder"})
    public PermissionHolderUpdateEvent(PermissionHolder permissionHolder) {
        this.permissionHolder = permissionHolder;
    }

    public PermissionHolder getPermissionHolder() {
        return permissionHolder;
    }
}
