/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.player.permissions.group;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.annotiations.MayNotBePresent;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

public final class PermissionGroup implements Serializable {

    /**
     * The name of the permission group
     */
    private String name;

    /**
     * The group prefix
     */
    private String prefix;

    /**
     * The group suffix
     */
    private String suffix;

    /**
     * The group display name
     */
    private String display;

    /**
     * The tab colour code of the group
     */
    @MayNotBePresent
    private String tabColorCode;

    /**
     * The group id
     */
    private int groupID;

    /**
     * The permissions of the group
     */
    private Map<String, Boolean> permissions;

    /**
     * ServerGroup permissions of the group
     */
    private Map<String, Map<String, Boolean>> groupPermissions;

    @java.beans.ConstructorProperties({"name", "prefix", "suffix", "display", "tabColorCode",
        "groupID", "permissions", "groupPermissions"})
    public PermissionGroup(String name, String prefix, String suffix, String display,
        String tabColorCode, int groupID, Map<String, Boolean> permissions,
        Map<String, Map<String, Boolean>> groupPermissions) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.display = display;
        this.tabColorCode = tabColorCode;
        this.groupID = groupID;
        this.permissions = permissions;
        this.groupPermissions = groupPermissions;
    }

    /**
     * Adds a permission to the group
     *
     * @param perm The permission which should be added
     */
    public void addPermission(String perm) {
        this.permissions.put(perm.toLowerCase(), true);
    }

    /**
     * Adds the group a permission
     *
     * @param perm The permission which should be added
     * @param set If the permission should be enabled or not
     */
    public void addPermission(String perm, boolean set) {
        this.permissions.put(perm.toLowerCase(), set);
    }

    /**
     * Adds a ServerGroup permission to the group
     *
     * @param serverGroup The server group for which the permission is to be used
     * @param perm The permission which should be added
     */
    public void addGroupPermission(String serverGroup, String perm) {
        if (this.groupPermissions.get(serverGroup) == null
            || this.groupPermissions.get(serverGroup).isEmpty()) {
            this.groupPermissions.put(serverGroup, Collections.singletonMap(perm, true));
        } else {
            Map<String, Boolean> newPermissions = this.groupPermissions.get(serverGroup);
            newPermissions.put(perm, true);
            this.groupPermissions.put(serverGroup, newPermissions);
        }
    }

    /**
     * Adds the group a ServerGroup permission
     *
     * @param serverGroup The ServerGroup for which the permission is to be used
     * @param perm The permission which should be added
     * @param set If the permission should be enabled or not
     */
    public void addGroupPermission(String serverGroup, String perm, boolean set) {
        if (this.groupPermissions.get(serverGroup) == null
            || this.groupPermissions.get(serverGroup).isEmpty()) {
            this.groupPermissions.put(serverGroup, Collections.singletonMap(perm, set));
        } else {
            Map<String, Boolean> newPermissions = this.groupPermissions.get(serverGroup);
            newPermissions.put(perm, set);
            this.groupPermissions.put(serverGroup, newPermissions);
        }
    }

    /**
     * Get the prefix of the group
     *
     * @return The prefix of the group
     */
    public String getPrefix() {
        if (prefix == null) {
            return StringUtil.EMPTY;
        }

        return prefix;
    }

    /**
     * Get the suffix of the group
     *
     * @return The suffix of the group
     */
    public String getSuffix() {
        if (suffix == null) {
            return StringUtil.EMPTY;
        }

        return suffix;
    }

    /**
     * Get the display of the group
     *
     * @return The display of the group
     */
    public String getDisplay() {
        if (display == null) {
            return StringUtil.EMPTY;
        }

        return display;
    }

    /**
     * Get the name of the group
     *
     * @return The name of the group
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the tab color code of the group
     *
     * @return The tab color code of the group
     */
    public String getTabColorCode() {
        return this.tabColorCode;
    }

    /**
     * Get the group id of the group
     *
     * @return The group id of the group
     */
    public int getGroupID() {
        return this.groupID;
    }

    /**
     * Get the permissions of the group
     *
     * @return The permissions of the group
     */
    public Map<String, Boolean> getPermissions() {
        return this.permissions;
    }

    /**
     * Get the servergroup permissions of the group
     *
     * @return The servergroup permissions of the group
     */
    public Map<String, Map<String, Boolean>> getGroupPermissions() {
        return groupPermissions;
    }

    /**
     * Set the name of the group
     *
     * @param name The new name of the group
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the prefix of the group
     *
     * @param prefix The new prefix of the group
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Set the suffix of the group
     *
     * @param suffix The new suffix of the group
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * Set the display of the group
     *
     * @param display The new display of the group
     */
    public void setDisplay(String display) {
        this.display = display;
    }

    /**
     * Set the tab color code of the group
     *
     * @param tabColorCode The new tab color code of the group
     */
    public void setTabColorCode(String tabColorCode) {
        this.tabColorCode = tabColorCode;
    }

    /**
     * Set the group id of the group
     *
     * @param groupID The new group id of the group
     */
    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    /**
     * Set the permissions of the group
     *
     * @param permissions The new permissions of the group
     */
    public void setPermissions(Map<String, Boolean> permissions) {
        this.permissions = permissions;
    }

    /**
     * Set the servergroup permissions of the group
     *
     * @param groupPermissions The new servergroup permissions of the group
     */
    public void setGroupPermissions(Map<String, Map<String, Boolean>> groupPermissions) {
        this.groupPermissions = groupPermissions;
    }
}