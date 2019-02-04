/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packet.enums.PacketSender;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.util.Collections;

/**
 * @author _Klaro | Pasqual K. / created on 08.12.2018
 */

public final class PacketOutExecuteCommand extends Packet {
    public PacketOutExecuteCommand(final String command, final String targetType, final String targetName) {
        super("ExecuteCommand", new Configuration().addStringProperty("command", command).addProperty("type", targetType).addProperty("target", targetName), Collections.singletonList(QueryType.COMPLETE), PacketSender.CONTROLLER);
    }
}
