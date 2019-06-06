/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.sync.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.utility.annotiations.ForRemoval;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 03.02.2019
 */

public final class PacketOutSyncUpdate extends Packet implements Serializable {

    private static final long serialVersionUID = 8220472124040186582L;

    @ForRemoval
    public PacketOutSyncUpdate() {
        super(
            "SyncUpdate",
            new Configuration()
        );
    }
}
