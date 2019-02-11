/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public final class PacketOutCopyServerIntoTemplate extends Packet {
    public PacketOutCopyServerIntoTemplate(final String name, final String type, final String group) {
        super("CopyServerIntoTemplate", new Configuration().addStringProperty("name", name).addStringProperty("type", type).addStringProperty("group", group));
    }
}
