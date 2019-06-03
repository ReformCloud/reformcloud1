/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

public final class PacketOutQueryGetPermissionCache extends Packet implements Serializable {

    public PacketOutQueryGetPermissionCache() {
        super("QueryGetPermissionCache", new Configuration());
    }
}
