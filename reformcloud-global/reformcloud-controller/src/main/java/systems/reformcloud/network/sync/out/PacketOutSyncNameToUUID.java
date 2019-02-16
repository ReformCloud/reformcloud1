/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.sync.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 16.02.2019
 */

public final class PacketOutSyncNameToUUID extends Packet implements Serializable {
    public PacketOutSyncNameToUUID(final UUID uuid, final String whoIam) {
        super(
                "SyncNameToUUID",
                new Configuration().addStringProperty("who", whoIam)
                        .addProperty("result", uuid)
        );
    }
}
