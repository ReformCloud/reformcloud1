/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.mobsaddon.packet.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class PacketOutRequestAll extends Packet implements Serializable {
    public PacketOutRequestAll() {
        super("RequestAll", new Configuration());
    }
}
