/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 29.01.2019
 */

public final class PacketOutUploadLog extends Packet implements Serializable {
    private static final long serialVersionUID = -3275070800933988588L;

    public PacketOutUploadLog(final String name, final String type) {
        super(
                "PacketInUploadLog",
                new Configuration().addStringProperty("name", name).addStringProperty("type", type)
        );
    }
}
