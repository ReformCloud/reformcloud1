/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.mobsaddon.packet.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.mobs.SelectorMob;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class PacketOutCreateMob extends DefaultPacket implements Serializable {

    public PacketOutCreateMob(SelectorMob selectorMob) {
        super(
            "CreateMob",
            new Configuration().addValue("mob", selectorMob)
        );
    }
}
