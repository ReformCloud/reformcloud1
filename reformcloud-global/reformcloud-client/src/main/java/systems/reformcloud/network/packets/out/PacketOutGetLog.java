/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 29.01.2019
 */

public final class PacketOutGetLog extends Packet implements Serializable {
    private static final long serialVersionUID = 7160830277462211122L;

    public PacketOutGetLog(final String url, final String process) {
        super(
                "ProcessLog",
                new Configuration().addStringProperty("process", process).addStringProperty("url", url)
        );
    }
}
