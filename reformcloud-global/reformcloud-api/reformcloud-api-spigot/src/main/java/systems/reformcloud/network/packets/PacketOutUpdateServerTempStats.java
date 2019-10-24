/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.server.stats.TempServerStats;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 11.02.2019
 */

public final class PacketOutUpdateServerTempStats extends DefaultPacket implements Serializable {

    public PacketOutUpdateServerTempStats(final TempServerStats tempServerStats) {
        super("UpdateTempServerStats", new Configuration().addValue("stats", tempServerStats));
    }
}
