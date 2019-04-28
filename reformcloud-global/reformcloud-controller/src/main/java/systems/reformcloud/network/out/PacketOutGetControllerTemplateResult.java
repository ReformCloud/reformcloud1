/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 15.03.2019
 */

public final class PacketOutGetControllerTemplateResult extends Packet implements Serializable {
    public PacketOutGetControllerTemplateResult(String encoded, String name, UUID processUID, String type, String group) {
        super("GetControllerTemplateResult",
                new Configuration()
                        .addStringValue("encode", encoded)
                        .addStringValue("name", name)
                        .addStringValue("group", group)
                        .addStringValue("type", type)
                        .addValue("uuid", processUID)
        );
    }
}
