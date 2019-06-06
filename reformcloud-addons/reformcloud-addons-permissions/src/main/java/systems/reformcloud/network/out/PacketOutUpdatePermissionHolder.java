/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import java.io.Serializable;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.player.permissions.player.PermissionHolder;

/**
 * @author _Klaro | Pasqual K. / created on 05.05.2019
 */

public final class PacketOutUpdatePermissionHolder extends Packet implements Serializable {

    public PacketOutUpdatePermissionHolder(PermissionHolder permissionHolder) {
        super("UpdatePermissionHolder", new Configuration().addValue("holder", permissionHolder));
    }
}
