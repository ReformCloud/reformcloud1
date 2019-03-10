/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.player.permissions.group;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

@AllArgsConstructor
@Getter
public final class PermissionGroup implements Serializable {
    private String name, prefix, suffix, display;
    private int groupID;

    private Map<String, Boolean> permissions;

    public void addPermission(String perm) {
        this.permissions.put(perm.toLowerCase(), true);
    }

    public void addPermission(String perm, boolean set) {
        this.permissions.put(perm.toLowerCase(), set);
    }
}