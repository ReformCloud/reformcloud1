/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public final class PacketOutCopyServerIntoTemplate extends DefaultPacket {

    public PacketOutCopyServerIntoTemplate(final String name, final String serverName,
        final String type, final String group) {
        super("CopyServerIntoTemplate", new Configuration()
            .addStringValue("name", name)
            .addStringValue("type", type)
            .addStringValue("serverName", serverName)
            .addStringValue("group", group));
    }

    public PacketOutCopyServerIntoTemplate(final String name, final String serverName,
        final String type, final String group, String specific) {
        super("CopyServerIntoTemplate", new Configuration()
            .addStringValue("name", name)
            .addStringValue("type", type)
            .addStringValue("serverName", serverName)
            .addStringValue("group", group)
            .addStringValue("specific", specific)
        );
    }
}
