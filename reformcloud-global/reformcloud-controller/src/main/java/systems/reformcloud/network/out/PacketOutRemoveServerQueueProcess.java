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

public final class PacketOutRemoveServerQueueProcess extends Packet implements Serializable {
    public PacketOutRemoveServerQueueProcess(final String name) {
        super(
                "RemoveServerQueueProcess",
                new Configuration().addStringProperty("name", name)
        );
    }
}
