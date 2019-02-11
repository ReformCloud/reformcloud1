/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.sync.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 03.02.2019
 */

public final class PacketOutSyncUpdate extends Packet implements Serializable {
    private static final long serialVersionUID = 8220472124040186582L;

    public PacketOutSyncUpdate() {
        super(
                "SyncUpdate",
                new Configuration()
        );
    }
}
