/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.sync.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 15.04.2019
 */

public final class PacketOutSyncStandby extends Packet implements Serializable {

    public PacketOutSyncStandby(boolean standby) {
        super("SyncStandby", new Configuration().addBooleanValue("standby", standby));
    }
}
