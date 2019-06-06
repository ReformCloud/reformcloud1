/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.packets.out;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.mobs.SelectorMob;
import systems.reformcloud.mobs.config.SelectorMobConfig;
import systems.reformcloud.network.packet.Packet;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class PacketOutUpdateMobs extends Packet implements Serializable {

    public PacketOutUpdateMobs(Map<UUID, SelectorMob> mobs, SelectorMobConfig selectorMobConfig) {
        super(
            "UpdateMobs",
            new Configuration()
                .addValue("mobs", mobs)
                .addValue("config", selectorMobConfig)
        );
    }
}
