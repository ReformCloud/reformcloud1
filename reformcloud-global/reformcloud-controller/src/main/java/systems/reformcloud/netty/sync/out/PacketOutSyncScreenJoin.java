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

public final class PacketOutSyncScreenJoin extends Packet implements Serializable {
    private static final long serialVersionUID = -705467496092941785L;

    public PacketOutSyncScreenJoin(final String name) {
        super(
                "JoinScreen",
                new Configuration()
                        .addStringProperty("name", name),
                Collections.singletonList(QueryType.COMPLETE),
                PacketSender.CONTROLLER
        );
    }
}
