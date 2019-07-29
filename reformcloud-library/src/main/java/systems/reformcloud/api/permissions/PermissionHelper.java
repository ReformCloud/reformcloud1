/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api.permissions;

import systems.reformcloud.player.permissions.PermissionCache;
import systems.reformcloud.player.permissions.group.PermissionGroup;
import systems.reformcloud.player.permissions.player.PermissionHolder;
import systems.reformcloud.utility.Require;
import systems.reformcloud.utility.annotiations.MayNotBePresent;
import systems.reformcloud.utility.annotiations.ShouldNotBeNull;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author _Klaro | Pasqual K. / created on 28.06.2019
 */

public interface PermissionHelper extends Serializable {

    AtomicReference<PermissionHelper> instance = new AtomicReference<>();

    @MayNotBePresent
    PermissionGroup getPermissionGroup(String name);

    @MayNotBePresent
    PermissionHolder getPermissionHolder(String name);

    @MayNotBePresent
    PermissionHolder getPermissionHolder(UUID uniqueID);

    @MayNotBePresent
    PermissionCache getPermissionCache();

    @MayNotBePresent
    default String getGroupPrefix(String groupName) {
        Require.requireNotNull(groupName);
        PermissionGroup permissionGroup = getPermissionGroup(groupName);
        if (permissionGroup == null) {
            return null;
        }

        return permissionGroup.getPrefix();
    }

    @MayNotBePresent
    default String getGroupDisplay(String groupName) {
        Require.requireNotNull(groupName);
        PermissionGroup permissionGroup = getPermissionGroup(groupName);
        if (permissionGroup == null) {
            return null;
        }

        return permissionGroup.getDisplay();
    }

    @MayNotBePresent
    default String getGroupSuffix(String groupName) {
        Require.requireNotNull(groupName);
        PermissionGroup permissionGroup = getPermissionGroup(groupName);
        if (permissionGroup == null) {
            return null;
        }

        return permissionGroup.getSuffix();
    }

    @MayNotBePresent
    default String getGroupTabColourCode(String groupName) {
        Require.requireNotNull(groupName);
        PermissionGroup permissionGroup = getPermissionGroup(groupName);
        if (permissionGroup == null) {
            return null;
        }

        return permissionGroup.getTabColorCode();
    }

    @MayNotBePresent
    default int getGroupID(String groupName) {
        Require.requireNotNull(groupName);
        PermissionGroup permissionGroup = getPermissionGroup(groupName);
        if (permissionGroup == null) {
            return Integer.MAX_VALUE;
        }

        return permissionGroup.getGroupID();
    }

    @MayNotBePresent
    default Map<String, Boolean> getGroupPermissions(String groupName) {
        Require.requireNotNull(groupName);
        PermissionGroup permissionGroup = getPermissionGroup(groupName);
        if (permissionGroup == null) {
            return Collections.emptyMap();
        }

        return new LinkedHashMap<>(permissionGroup.getPermissions());
    }

    @MayNotBePresent
    default PermissionGroup getHighestPlayerGroup(String userName) {
        Require.requireNotNull(userName);
        PermissionHolder permissionHolder = getPermissionHolder(userName);
        if (permissionHolder == null) {
            return null;
        }

        return permissionHolder.getHighestPlayerGroup(getPermissionCache()).orElse(null);
    }

    @MayNotBePresent
    default String getPlayerGroupName(String userName) {
        Require.requireNotNull(userName);
        PermissionHolder permissionHolder = getPermissionHolder(userName);
        if (permissionHolder == null) {
            return null;
        }

        PermissionGroup permissionGroup = getHighestPlayerGroup(userName);
        return permissionGroup == null ? null : permissionGroup.getName();
    }

    @MayNotBePresent
    default String getPlayerGroupPrefix(String userName) {
        Require.requireNotNull(userName);
        PermissionHolder permissionHolder = getPermissionHolder(userName);
        if (permissionHolder == null) {
            return null;
        }

        PermissionGroup permissionGroup = getHighestPlayerGroup(userName);
        return permissionGroup == null ? null : permissionGroup.getPrefix();
    }

    @MayNotBePresent
    default String getPlayerGroupDisplay(String userName) {
        Require.requireNotNull(userName);
        PermissionHolder permissionHolder = getPermissionHolder(userName);
        if (permissionHolder == null) {
            return null;
        }

        PermissionGroup permissionGroup = getHighestPlayerGroup(userName);
        return permissionGroup == null ? null : permissionGroup.getDisplay();
    }

    @MayNotBePresent
    default String getPlayerGroupSuffix(String userName) {
        Require.requireNotNull(userName);
        PermissionHolder permissionHolder = getPermissionHolder(userName);
        if (permissionHolder == null) {
            return null;
        }

        PermissionGroup permissionGroup = getHighestPlayerGroup(userName);
        return permissionGroup == null ? null : permissionGroup.getSuffix();
    }

    @MayNotBePresent
    default int getPlayerGroupID(String userName) {
        Require.requireNotNull(userName);
        PermissionHolder permissionHolder = getPermissionHolder(userName);
        if (permissionHolder == null) {
            return Integer.MIN_VALUE;
        }

        PermissionGroup permissionGroup = getHighestPlayerGroup(userName);
        return permissionGroup == null ? Integer.MIN_VALUE :
            permissionGroup.getGroupID();
    }

    @MayNotBePresent
    default String getPlayerGroupTabColourCode(String userName) {
        Require.requireNotNull(userName);
        PermissionHolder permissionHolder = getPermissionHolder(userName);
        if (permissionHolder == null) {
            return null;
        }

        PermissionGroup permissionGroup = getHighestPlayerGroup(userName);
        return permissionGroup == null ? null : permissionGroup.getTabColorCode();
    }

    @MayNotBePresent
    default Map<String, Boolean> getPlayerGroupPermissions(String userName) {
        Require.requireNotNull(userName);
        PermissionHolder permissionHolder = getPermissionHolder(userName);
        if (permissionHolder == null) {
            return null;
        }

        PermissionGroup permissionGroup = getHighestPlayerGroup(userName);
        return permissionGroup == null ? null
            : new LinkedHashMap<>(permissionGroup.getPermissions());
    }

    default boolean hasPermission(String userName, String permission) {
        Require.requireNotNull(userName);
        Require.requireNotNull(permission);
        PermissionHolder permissionHolder = getPermissionHolder(userName);
        if (permissionHolder == null) {
            return false;
        }

        return permissionHolder.hasPermission(permission,
            permissionHolder.getAllPermissionGroups(getPermissionCache()));
    }

    void executeCommand(@ShouldNotBeNull String commandLine);

    default void setGroupPrefix(String group, String newPrefix) {
        Require.requireNotNull(group);
        Require.requireNotNull(newPrefix);

        PermissionGroup permissionGroup = getPermissionGroup(group);
        if (permissionGroup == null) {
            return;
        }

        executeCommand("perms " + group + " setprefix " + newPrefix);
    }

    default void setGroupDisplay(String group, String newDisplay) {
        Require.requireNotNull(group);
        Require.requireNotNull(newDisplay);

        PermissionGroup permissionGroup = getPermissionGroup(group);
        if (permissionGroup == null) {
            return;
        }

        executeCommand("perms " + group + " setdisplay " + newDisplay);
    }

    default void setGroupSuffix(String group, String newSuffix) {
        Require.requireNotNull(group);
        Require.requireNotNull(newSuffix);

        PermissionGroup permissionGroup = getPermissionGroup(group);
        if (permissionGroup == null) {
            return;
        }

        executeCommand("perms " + group + " setsuffix " + newSuffix);
    }

    default void setGroupTabColourCode(String group, String newTabCode) {
        Require.requireNotNull(group);
        Require.requireNotNull(newTabCode);

        PermissionGroup permissionGroup = getPermissionGroup(group);
        if (permissionGroup == null) {
            return;
        }

        executeCommand("perms " + group + " SETTABCOLORCODE " + newTabCode);
    }

    default void setGroupGroupID(String group, int newGroupID) {
        Require.requireNotNull(group);
        Require.requireNotNull(newGroupID);

        PermissionGroup permissionGroup = getPermissionGroup(group);
        if (permissionGroup == null) {
            return;
        }

        executeCommand("perms " + group + " setgroupid " + newGroupID);
    }

    default void setDefaultGroup(String newGroup) {
        Require.requireNotNull(newGroup);

        PermissionGroup permissionGroup = getPermissionGroup(newGroup);
        if (permissionGroup == null) {
            return;
        }

        setDefaultGroup(permissionGroup);
    }

    default void setDefaultGroup(PermissionGroup newGroup) {
        Require.requireNotNull(newGroup);

        executeCommand("perms " + newGroup.getName() + " setdefault");
    }

    default void createPermissionGroup(String name) {
        Require.requireNotNull(name);
        if (getPermissionGroup(name) != null) {
            return;
        }

        executeCommand("perms " + name + " create");
    }

    default void deletePermissionGroup(String name) {
        Require.requireNotNull(name);
        if (getPermissionGroup(name) == null) {
            return;
        }

        executeCommand("perms " + name + " delete");
    }

    default void addGroupPermission(String groupName, String permission) {
        Require.requireNotNull(groupName);
        Require.requireNotNull(permission);
        if (getPermissionGroup(groupName) == null) {
            return;
        }

        executeCommand("perms " + groupName + " add " + permission);
    }

    default void removeGroupPermission(String groupName, String permission) {
        Require.requireNotNull(groupName);
        Require.requireNotNull(permission);
        if (getPermissionGroup(groupName) == null) {
            return;
        }

        executeCommand("perms " + groupName + " remove " + permission);
    }

    default void addPlayerPermission(String name, String permission) {
        Require.requireNotNull(name);
        Require.requireNotNull(permission);

        executeCommand("perms " + name + " addperm " + permission);
    }

    default void removePlayerPermission(String name, String permission) {
        Require.requireNotNull(name);
        Require.requireNotNull(permission);

        executeCommand("perms " + name + " removeperm " + permission);
    }

    default void addPlayerGroup(String userName, String permissionGroup) {
        Require.requireNotNull(userName);
        Require.requireNotNull(permissionGroup);

        executeCommand("perms " + userName + " addgroup " + permissionGroup);
    }

    default void setPlayerGroup(String userName, String permissionGroup) {
        Require.requireNotNull(userName);
        Require.requireNotNull(permissionGroup);

        executeCommand("perms " + userName + " setgroup " + permissionGroup);
    }

    default void removePlayerGroup(String userName, String permissionGroup) {
        Require.requireNotNull(userName);
        Require.requireNotNull(permissionGroup);

        executeCommand("perms " + userName + " removegroup " + permissionGroup);
    }

    default void addPlayerGroup(String userName, String permissionGroup,
        int timeoutInDays) {
        Require.requireNotNull(userName);
        Require.requireNotNull(permissionGroup);
        Require.requireNotNull(timeoutInDays);

        executeCommand("perms " + userName + " addgroup " + permissionGroup + " " + timeoutInDays);
    }

    default void setPlayerGroup(String userName, String permissionGroup,
        int timeoutInDays) {
        Require.requireNotNull(userName);
        Require.requireNotNull(permissionGroup);

        executeCommand("perms " + userName + " setgroup " + permissionGroup + " " + timeoutInDays);
    }
}
