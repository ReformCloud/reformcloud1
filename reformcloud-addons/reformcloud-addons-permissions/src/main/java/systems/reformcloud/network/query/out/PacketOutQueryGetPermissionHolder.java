/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;
import systems.reformcloud.player.permissions.player.PermissionHolder;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

public final class PacketOutQueryGetPermissionHolder extends DefaultPacket implements Serializable {

    public PacketOutQueryGetPermissionHolder(PermissionHolder permissionHolder, UUID resultID) {
        super("undefined", new Configuration().addValue("holder", permissionHolder));
        super.setResult(resultID);
    }
}
