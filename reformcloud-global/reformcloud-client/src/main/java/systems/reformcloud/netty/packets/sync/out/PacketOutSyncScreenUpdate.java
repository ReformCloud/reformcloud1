/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets.sync.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packet.enums.PacketSender;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.io.Serializable;
import java.util.Collections;

/**
 * @author _Klaro | Pasqual K. / created on 05.02.2019
 */

public final class PacketOutSyncScreenUpdate extends Packet implements Serializable {
    private static final long serialVersionUID = 3205044307706658304L;

    public PacketOutSyncScreenUpdate(final String line, final String who) {
        super(
                "ScreenUpdate",
                new Configuration()
                        .addStringProperty("line", line)
                        .addStringProperty("from", who),
                Collections.singletonList(QueryType.COMPLETE),
                PacketSender.CLIENT
        );
    }
}
