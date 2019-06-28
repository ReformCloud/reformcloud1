/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api;

import java.io.Serializable;
import java.util.UUID;
import systems.reformcloud.PermissionsAddon;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.api.permissions.PermissionHelper;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.player.permissions.PermissionCache;
import systems.reformcloud.player.permissions.group.PermissionGroup;
import systems.reformcloud.player.permissions.player.PermissionHolder;

/**
 * @author _Klaro | Pasqual K. / created on 28.06.2019
 */

public final class DefaultPermissionHelper implements Serializable, PermissionHelper {

    @Override
    public PermissionGroup getPermissionGroup(String name) {
        return getPermissionCache().getGroup(name);
    }

    @Override
    public PermissionHolder getPermissionHolder(String name) {
        OfflinePlayer offlinePlayer =
            ReformCloudController.getInstance().getOfflinePlayer(name);
        if (offlinePlayer == null) {
            return null;
        }

        return getPermissionHolder(offlinePlayer.getUniqueID());
    }

    @Override
    public PermissionHolder getPermissionHolder(UUID uniqueID) {
        return PermissionsAddon.getInstance().getPermissionDatabase().getPermissionHolder(uniqueID);
    }

    @Override
    public PermissionCache getPermissionCache() {
        return PermissionsAddon.getInstance().getPermissionDatabase().getPermissionCache();
    }
}
