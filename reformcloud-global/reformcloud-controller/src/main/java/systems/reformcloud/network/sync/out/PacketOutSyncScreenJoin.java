/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.sync.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 05.02.2019
 */

public final class PacketOutSyncScreenJoin extends DefaultPacket implements Serializable {

    private static final long serialVersionUID = -705467496092941785L;

    public PacketOutSyncScreenJoin(final String name) {
        super(
            "JoinScreen",
            new Configuration()
                .addStringValue("name", name)
        );
    }
}
