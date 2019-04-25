/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.PermissionsAddon;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

public final class PacketOutUpdatePermissionCache extends Packet implements Serializable {
    public PacketOutUpdatePermissionCache() {
        super("UpdatePermissionCache", new Configuration().addValue("cache",
                PermissionsAddon.getInstance().getPermissionDatabase().getPermissionCache()));
    }
}
