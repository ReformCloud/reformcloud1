/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.server.stats.TempServerStats;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 11.02.2019
 */

public final class PacketOutUpdateServerTempStats extends Packet implements Serializable {
    public PacketOutUpdateServerTempStats(final TempServerStats tempServerStats) {
        super("UpdateTempServerStats", new Configuration().addProperty("stats", tempServerStats));
    }
}
