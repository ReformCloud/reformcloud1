/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.out;

import systems.reformcloud.PermissionsAddon;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

public final class PacketOutQueryGetPermissionCache extends DefaultPacket implements Serializable {

    public PacketOutQueryGetPermissionCache(UUID result) {
        super("undefined", new Configuration().addValue("cache",
            PermissionsAddon.getInstance().getPermissionDatabase().getPermissionCache()));
        super.setResult(result);
    }
}
