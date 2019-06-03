/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.out;

import java.io.Serializable;
import java.util.UUID;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.player.permissions.player.PermissionHolder;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

public final class PacketOutQueryGetPermissionHolder extends Packet implements Serializable {

    public PacketOutQueryGetPermissionHolder(PermissionHolder permissionHolder, UUID resultID) {
        super("undefined", new Configuration().addValue("holder", permissionHolder));
        super.setResult(resultID);
    }
}
