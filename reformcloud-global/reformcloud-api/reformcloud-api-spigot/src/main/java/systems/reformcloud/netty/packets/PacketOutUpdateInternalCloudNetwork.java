/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public final class PacketOutUpdateInternalCloudNetwork extends Packet {
    public PacketOutUpdateInternalCloudNetwork(final InternalCloudNetwork internalCloudNetwork) {
        super("UpdateInternalCloudNetwork", new Configuration().addProperty("networkProperties", internalCloudNetwork));
    }
}
