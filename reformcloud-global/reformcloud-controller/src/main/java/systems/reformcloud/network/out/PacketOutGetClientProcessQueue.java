/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 07.02.2019
 */

public final class PacketOutGetClientProcessQueue extends Packet implements Serializable {
    public PacketOutGetClientProcessQueue() {
        super(
                "ClientProcessQueue",
                new Configuration()
        );
    }
}
