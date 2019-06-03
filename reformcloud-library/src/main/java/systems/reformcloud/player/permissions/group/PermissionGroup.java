/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.player.permissions.group;

import systems.reformcloud.utility.StringUtil;

import java.io.Serializable;
import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

public final class PermissionGroup implements Serializable {

    /**
     * General info about the group
     */
    private String name;

    private String prefix;

    private String suffix;

    private String display;

    private String tabColorCode;

    /**
     * The group id
     */
    private int groupID;

    /**
     * The permissions of the group
     */
    private Map<String, Boolean> permissions;

    @java.beans.ConstructorProperties({"name", "prefix", "suffix", "display", "tabColorCode",
        "groupID", "permissions"})
    public PermissionGroup(String name, String prefix, String suffix, String display,
        String tabColorCode, int groupID, Map<String, Boolean> permissions) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.display = display;
        this.tabColorCode = tabColorCode;
        this.groupID = groupID;
        this.permissions = permissions;
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

    public String getName() {
        return this.name;
    }

    public String getTabColorCode() {
        return this.tabColorCode;
    }

    public int getGroupID() {
        return this.groupID;
    }

    public Map<String, Boolean> getPermissions() {
        return this.permissions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public void setTabColorCode(String tabColorCode) {
        this.tabColorCode = tabColorCode;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public void setPermissions(Map<String, Boolean> permissions) {
        this.permissions = permissions;
    }
}