/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.player.permissions.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.reformcloud.utility.StringUtil;

import java.io.Serializable;
import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

@AllArgsConstructor
@Getter
public final class PermissionGroup implements Serializable {
    /**
     * General info about the group
     */
    private String name, prefix, suffix, display, tabColorCode;

    /**
     * The group id
     */
    private int groupID;

    /**
     * The permissions of the group
     */
    private Map<String, Boolean> permissions;

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
     * @param set  If the permission should be enabled or not
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
        if (prefix == null)
            return StringUtil.EMPTY;

        return prefix;
    }

    /**
     * Get the suffix of the group
     *
     * @return The suffix of the group
     */
    public String getSuffix() {
        if (suffix == null)
            return StringUtil.EMPTY;

        return suffix;
    }

    /**
     * Get the display of the group
     *
     * @return The display of the group
     */
    public String getDisplay() {
        if (display == null)
            return StringUtil.EMPTY;

        return display;
    }
}