/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 25.04.2019
 */

public final class PacketOutQueryStartQueuedProcess extends Packet implements Serializable {
    public PacketOutQueryStartQueuedProcess(ServerGroup serverGroup, String template, Configuration preConfig) {
        super("QueryStartQueuedProcess", new Configuration()
                .addValue("group", serverGroup)
                .addStringValue("template", template)
                .addValue("pre", preConfig));
    }
}
