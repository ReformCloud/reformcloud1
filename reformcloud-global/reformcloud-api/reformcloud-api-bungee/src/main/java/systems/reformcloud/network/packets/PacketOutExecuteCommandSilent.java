/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 04.05.2019
 */

public final class PacketOutExecuteCommandSilent extends DefaultPacket implements Serializable {

    public PacketOutExecuteCommandSilent(String line) {
        super("ExecuteCommandSilent", new Configuration().addStringValue("line", line));
    }
}
