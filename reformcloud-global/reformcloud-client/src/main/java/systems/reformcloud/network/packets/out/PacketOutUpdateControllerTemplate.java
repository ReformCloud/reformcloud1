/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;
import java.util.Base64;

/**
 * @author _Klaro | Pasqual K. / created on 15.03.2019
 */

public final class PacketOutUpdateControllerTemplate extends Packet implements Serializable {
    public PacketOutUpdateControllerTemplate(String type, String group, String template, byte[] files) {
        super("UpdateControllerTemplate", new Configuration()
                .addStringValue("type", type)
                .addStringValue("group", group)
                .addStringValue("template", template)
                .addStringValue("encoded", Base64.getEncoder().encodeToString(files))
        );
    }
}
