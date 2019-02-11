/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.sync.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.02.2019
 */

public final class PacketOutSyncControllerTime extends Packet implements Serializable {
    public PacketOutSyncControllerTime() {
        super(
                "SyncControllerTime",
                new Configuration().addLongProperty("time", System.currentTimeMillis())
        );
    }
}
