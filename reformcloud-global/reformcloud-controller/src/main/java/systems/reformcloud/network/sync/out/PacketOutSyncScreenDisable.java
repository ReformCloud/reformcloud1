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

public final class PacketOutSyncScreenDisable extends DefaultPacket implements Serializable {

    private static final long serialVersionUID = 2358329581554616977L;

    public PacketOutSyncScreenDisable(final String name) {
        super(
            "ScreenDisable",
            new Configuration()
                .addStringValue("name", name)
        );
    }
}
