/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 14.04.2019
 */

public final class PacketOutRequestParameters extends DefaultPacket implements Serializable {

    public PacketOutRequestParameters() {
        super("RequestParameters", new Configuration());
    }
}
