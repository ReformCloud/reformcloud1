/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 22.06.2019
 */

public final class PacketOutExecuteCommand extends DefaultPacket implements Serializable {

    public PacketOutExecuteCommand(String type, String name,
                                   String commandLine) {
        super(
            "ExecuteCommand",
            new Configuration().addStringValue("type", type).addStringValue("name", name)
                .addStringValue("command", commandLine)
        );
    }
}
