/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 22.04.2019
 */

public final class PacketOutRequestProperties extends Packet implements Serializable {

    public PacketOutRequestProperties() {
        super("RequestProperties", new Configuration());
    }
}
