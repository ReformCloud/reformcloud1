/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.sync.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 06.02.2019
 */

public final class PacketOutSyncUpdateClient extends Packet implements Serializable {
    private static final long serialVersionUID = -7697566232691674568L;

    public PacketOutSyncUpdateClient() {
        super(
                "ReloadClient",
                new Configuration()
        );
    }
}
