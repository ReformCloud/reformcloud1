/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 09.03.2019
 */

public final class PacketOutQueryGetSigns extends Packet implements Serializable {
    public PacketOutQueryGetSigns() {
        super("QueryGetSigns", new Configuration());
    }
}
