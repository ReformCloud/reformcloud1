/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.player.permissions;

import systems.reformcloud.player.permissions.group.PermissionGroup;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

public final class PermissionCache implements Serializable {
    private boolean chatEnabled, tabEnabled;
    private String chatFormat;

    private List<PermissionGroup> allRegisteredGroups;

    private PermissionGroup defaultGroup;

    @java.beans.ConstructorProperties({"chatEnabled", "tabEnabled", "chatFormat", "allRegisteredGroups", "defaultGroup"})
    public PermissionCache(boolean chatEnabled, boolean tabEnabled, String chatFormat, List<PermissionGroup> allRegisteredGroups, PermissionGroup defaultGroup) {
        this.chatEnabled = chatEnabled;
        this.tabEnabled = tabEnabled;
        this.chatFormat = chatFormat;
        this.allRegisteredGroups = allRegisteredGroups;
        this.defaultGroup = defaultGroup;
    }

    public boolean isChatEnabled() {
        return this.chatEnabled;
    }

    public boolean isTabEnabled() {
        return this.tabEnabled;
    }

    public String getChatFormat() {
        return this.chatFormat;
    }

    public List<PermissionGroup> getAllRegisteredGroups() {
        return this.allRegisteredGroups;
    }

    public PermissionGroup getDefaultGroup() {
        return this.defaultGroup;
    }

    public void setDefaultGroup(PermissionGroup defaultGroup) {
        this.defaultGroup = defaultGroup;
    }
}
