/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.mobsaddon.packet.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class PacketOutDeleteMob extends Packet implements Serializable {
    public PacketOutDeleteMob(UUID selectorMob) {
        super(
                "DeleteMob",
                new Configuration().addValue("mob", selectorMob)
        );
    }
}
