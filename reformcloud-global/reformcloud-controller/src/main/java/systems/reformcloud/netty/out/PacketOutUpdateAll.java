/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packet.enums.PacketSender;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;

import java.util.Collections;

/**
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

public final class PacketOutUpdateAll extends Packet {
    public PacketOutUpdateAll(final InternalCloudNetwork internalCloudNetwork) {
        super("UpdateAll", new Configuration().addProperty("networkProperties", internalCloudNetwork), Collections.singletonList(QueryType.COMPLETE), PacketSender.CONTROLLER);
    }
}
