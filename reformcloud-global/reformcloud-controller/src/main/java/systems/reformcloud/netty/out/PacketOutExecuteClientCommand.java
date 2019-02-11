/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 06.02.2019
 */

public final class PacketOutExecuteClientCommand extends Packet implements Serializable {
    public PacketOutExecuteClientCommand(String command) {
        super(
                "ExecuteClientCommand",
                new Configuration().addStringProperty("cmd", command)
        );
    }
}
