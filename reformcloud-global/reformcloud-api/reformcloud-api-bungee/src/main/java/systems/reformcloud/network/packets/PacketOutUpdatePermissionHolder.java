/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;
import systems.reformcloud.player.permissions.player.PermissionHolder;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 15.03.2019
 */

public final class PacketOutUpdatePermissionHolder extends DefaultPacket implements Serializable {

    public PacketOutUpdatePermissionHolder(PermissionHolder permissionHolder) {
        super("UpdatePermissionHolder", new Configuration().addValue("holder", permissionHolder));
    }
}
