/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.packets.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.mobs.SelectorMob;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class PacketOutCreateMob extends Packet implements Serializable {
    public PacketOutCreateMob(SelectorMob selectorMob) {
        super(
                "CreateMob",
                new Configuration().addProperty("mob", selectorMob)
        );
    }
}
