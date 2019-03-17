/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.player.permissions.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.reformcloud.player.permissions.group.PermissionGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

@AllArgsConstructor
@Getter
public final class PermissionHolder implements Serializable {
    private UUID uniqueID;

    private Map<String, Long> permissionGroups;

    private Map<String, Boolean> playerPermissions;

    public boolean hasPermission(String permission, List<PermissionGroup> playerGroups) {
        if (permission != null && (permission.equalsIgnoreCase("bukkit.brodcast")
                || permission.equalsIgnoreCase("bukkit.brodcast.admin"))) {
            return true;
        }

        if (permission == null || playerGroups == null)
            return false;

        permission = permission.toLowerCase();

        for (PermissionGroup permissionGroup : playerGroups) {
            if (checkPermission(permissionGroup.getPermissions(), permission))
                return true;
        }

        return checkPermission(this.playerPermissions, permission);
    }

    private boolean checkPermission(Map<String, Boolean> permissions, String permission) {
        for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
            if (entry.getKey().equalsIgnoreCase("*") && entry.getValue())
                return true;

            if (entry.getKey().endsWith("*") && entry.getKey().length() > 1
                    && permission.startsWith(entry.getKey().substring(0, entry.getKey().length() - 1))
                    && entry.getValue()) {
                return true;
            }

            if (entry.getKey().equalsIgnoreCase(permission) && entry.getValue())
                return true;
        }

        return false;
    }

    public void addPermission(String perm) {
        this.playerPermissions.put(perm.toLowerCase(), true);
    }

    public void addPermission(String perm, boolean set) {
        this.playerPermissions.put(perm.toLowerCase(), set);
    }

    public List<String> getAllPermissions(List<PermissionGroup> permissionGroups) {
        List<String> permissions = new ArrayList<>();
        for (Map.Entry<String, Boolean> perms : this.playerPermissions.entrySet())
            permissions.add(perms.getValue() ? "" : "-" + perms.getKey());

        permissionGroups.forEach(permissionGroup -> {
            for (Map.Entry<String, Boolean> perms : permissionGroup.getPermissions().entrySet())
                permissions.add(perms.getValue() ? "" : "-" + perms.getKey());
        });

        return permissions;
    }
}
