/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packet.enums.PacketSender;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;

import java.util.Collections;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public final class PacketOutUpdateInternalCloudNetwork extends Packet {
    public PacketOutUpdateInternalCloudNetwork(final InternalCloudNetwork internalCloudNetwork) {
        super("UpdateInternalCloudNetwork", new Configuration().addProperty("networkProperties", internalCloudNetwork), Collections.singletonList(QueryType.COMPLETE), PacketSender.PROCESS_SERVER);
    }
}
