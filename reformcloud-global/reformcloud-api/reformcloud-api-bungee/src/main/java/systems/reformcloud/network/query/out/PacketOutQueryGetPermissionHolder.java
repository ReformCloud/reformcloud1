/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.player.permissions.player.PermissionHolder;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

public final class PacketOutQueryGetPermissionHolder extends Packet implements Serializable {
    public PacketOutQueryGetPermissionHolder(PermissionHolder permissionHolder) {
        super("QueryGetPermissionHolder", new Configuration().addValue("holder", permissionHolder));
    }
}
