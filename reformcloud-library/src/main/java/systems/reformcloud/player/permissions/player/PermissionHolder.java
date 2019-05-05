/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.player.permissions.player;

import systems.reformcloud.player.permissions.PermissionCache;
import systems.reformcloud.player.permissions.group.PermissionGroup;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

public final class PermissionHolder implements Serializable {
    /**
     * The uuid of the player
     */
    private UUID uniqueID;

    /**
     * The permission groups of the player
     */
    private Map<String, Long> permissionGroups;

    /**
     * The player permissions
     */
    private Map<String, Boolean> playerPermissions;

    @java.beans.ConstructorProperties({"uniqueID", "permissionGroups", "playerPermissions"})
    public PermissionHolder(UUID uniqueID, Map<String, Long> permissionGroups, Map<String, Boolean> playerPermissions) {
        this.uniqueID = uniqueID;
        this.permissionGroups = permissionGroups;
        this.playerPermissions = playerPermissions;
    }

    /**
     * Checks if a player has the given permission
     *
     * @param permission   The permission which should be checked
     * @param playerGroups The groups of the player
     * @return If the player has the permission
     */
    public boolean hasPermission(String permission, List<PermissionGroup> playerGroups) {
        if (permission != null && (permission.equalsIgnoreCase("bukkit.brodcast")
                || permission.equalsIgnoreCase("bukkit.brodcast.admin"))) {
            return true;
        }

        if (permission == null || playerGroups == null)
            return false;

        permission = permission.toLowerCase();

        for (PermissionGroup permissionGroup : playerGroups) {
            if (checkPermission0(permissionGroup.getPermissions(), permission))
                return true;
        }

        return checkPermission0(this.playerPermissions, permission);
    }

    /**
     * Checks if the player has the given permission
     *
     * @param permissions       The permission in which the cloud should search for the permission
     * @param permission        The permission which should be checked
     * @return If the player has the permission or not
     */
    private boolean checkPermission0(Map<String, Boolean> permissions, String permission) {
        if (permissions.containsKey("*") && permissions.get("*"))
            return true;

        for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
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

    /**
     * Adds the player a specific permission
     *
     * @param perm  The permission which should be added
     */
    public void addPermission(String perm) {
        this.playerPermissions.put(perm.toLowerCase(), true);
    }

    /**
     * Adds the player a permission
     *
     * @param perm  The permission which should be added
     * @param set   If the permission should be enabled or not
     */
    public void addPermission(String perm, boolean set) {
        this.playerPermissions.put(perm.toLowerCase(), set);
    }

    /**
     * Returns all permissions of the player and the given permission group
     *
     * @param permissionGroups      The permission groups which should be checked
     * @return A list containing all permission as string
     */
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

    /**
     * Get all registered permission groups
     *
     * @param permissionCache       The permission cache of the cloud
     * @return A list containing all registered permission groups
     */
    public List<PermissionGroup> getAllPermissionGroups(PermissionCache permissionCache) {
        List<PermissionGroup> permissionGroups = permissionCache.getAllRegisteredGroups().stream().filter(e -> this
                .getPermissionGroups()
                .containsKey(e.getName()))
                .collect(Collectors.toList());

        if (this.getPermissionGroups().containsKey(permissionCache.getDefaultGroup().getName()))
            permissionGroups.add(permissionCache.getDefaultGroup());

        return permissionGroups;
    }

    /**
     * Get the highest permission group of the player
     *
     * @param permissionCache   The permission cache of the cloud
     * @return An optional containing the highest permission group of the player
     */
    public Optional<PermissionGroup> getHighestPlayerGroup(PermissionCache permissionCache) {
        List<PermissionGroup> permissionGroups = this.getAllPermissionGroups(permissionCache);

        PermissionGroup permissionGroup = null;
        for (PermissionGroup group : permissionGroups) {
            if (permissionGroup == null)
                permissionGroup = group;
            else {
                if (group.getGroupID() > permissionGroup.getGroupID())
                    permissionGroup = group;
            }
        }

        return Optional.ofNullable(permissionGroup);
    }

    public UUID getUniqueID() {
        return this.uniqueID;
    }

    public Map<String, Long> getPermissionGroups() {
        return this.permissionGroups;
    }

    public Map<String, Boolean> getPlayerPermissions() {
        return this.playerPermissions;
    }
}
