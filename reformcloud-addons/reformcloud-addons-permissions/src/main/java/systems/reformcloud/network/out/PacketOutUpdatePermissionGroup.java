/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import java.io.Serializable;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.player.permissions.group.PermissionGroup;

/**
 * @author _Klaro | Pasqual K. / created on 17.05.2019
 */

public final class PacketOutUpdatePermissionGroup extends Packet implements Serializable {
    public PacketOutUpdatePermissionGroup(PermissionGroup permissionGroup) {
        super("UpdatePermissionGroup", new Configuration().addValue("group", permissionGroup));
    }
}
