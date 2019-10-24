/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 07.02.2019
 */

public final class PacketOutRemoveProxyQueueProcess extends DefaultPacket implements Serializable {

    public PacketOutRemoveProxyQueueProcess(final String name) {
        super(
            "RemoveProxyQueueProcess",
            new Configuration().addStringValue("name", name)
        );
    }
}
