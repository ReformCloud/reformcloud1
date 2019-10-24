/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 03.05.2019
 */

public final class PacketOutDeleteTemplate extends DefaultPacket implements Serializable {

    public PacketOutDeleteTemplate(String type, String template, String group) {
        super(
            "DeleteTemplate",
            new Configuration()
                .addStringValue("type", type.toLowerCase())
                .addStringValue("group", group)
                .addStringValue("template", template)
        );
    }
}
