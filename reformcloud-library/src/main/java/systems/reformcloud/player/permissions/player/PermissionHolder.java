/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.player.permissions.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.player.permissions.group.PermissionGroup;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

@AllArgsConstructor
@Getter
public final class PermissionHolder implements Serializable {
    private OfflinePlayer offlinePlayer;

    private List<PermissionGroup> permissionGroups;

    private Map<String, Boolean> playerPermissions;

    public boolean hasPermission(String permission) {
        permission = permission.toLowerCase();
        if (checkPermission(this.playerPermissions, permission))
            return true;

        {
            String[] block = permission.split(".");

            for (String perm : block) {
                if (checkPermission(this.playerPermissions, perm + ".*"))
                    return true;
            }
        }

        {
            for (PermissionGroup permissionGroup : this.permissionGroups) {
                if (checkPermission(permissionGroup.getPermissions(), permission))
                    return true;

                String[] block = permission.split(".");

                for (String perm : block)
                    if (checkPermission(permissionGroup.getPermissions(), perm + ".*"))
                        return true;
            }
        }

        return false;
    }

    private boolean checkPermission(Map<String, Boolean> permissions, String permission) {
        if (permissions.containsKey(permission))
            return permissions.get(permission);

        else if (permissions.containsKey("*"))
            return permissions.get("*");

        return false;
    }

    public void addPermission(String perm) {
        this.playerPermissions.put(perm.toLowerCase(), true);
    }

    public void addPermission(String perm, boolean set) {
        this.playerPermissions.put(perm.toLowerCase(), set);
    }
}
