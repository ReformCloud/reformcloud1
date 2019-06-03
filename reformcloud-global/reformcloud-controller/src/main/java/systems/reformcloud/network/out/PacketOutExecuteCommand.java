/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

/**
 * @author _Klaro | Pasqual K. / created on 08.12.2018
 */

public final class PacketOutExecuteCommand extends Packet {

    public PacketOutExecuteCommand(final String command, final String targetType,
        final String targetName) {
        super("ExecuteCommand",
            new Configuration().addStringValue("command", command).addValue("type", targetType)
                .addValue("target", targetName));
    }
}
