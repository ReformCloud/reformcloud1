/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 06.02.2019
 */

public final class PacketOutExecuteClientCommand extends DefaultPacket implements Serializable {

    public PacketOutExecuteClientCommand(String command) {
        super(
            "ExecuteClientCommand",
            new Configuration().addStringValue("cmd", command)
        );
    }
}
