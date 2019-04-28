/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;

/**
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

public final class PacketOutUpdateAll extends Packet {
    public PacketOutUpdateAll(final InternalCloudNetwork internalCloudNetwork) {
        super("UpdateAll", new Configuration().addValue("networkProperties", internalCloudNetwork));
    }
}
