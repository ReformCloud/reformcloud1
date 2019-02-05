/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.sync.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packet.enums.PacketSender;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.io.Serializable;
import java.util.Collections;

/**
 * @author _Klaro | Pasqual K. / created on 05.02.2019
 */

public final class PacketOutSyncScreenDisable extends Packet implements Serializable {
    private static final long serialVersionUID = 2358329581554616977L;

    public PacketOutSyncScreenDisable(final String name) {
        super(
                "ScreenDisable",
                new Configuration()
                        .addStringProperty("name", name),
                Collections.singletonList(QueryType.COMPLETE),
                PacketSender.CONTROLLER
        );
    }
}
