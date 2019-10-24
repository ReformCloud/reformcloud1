/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 22.04.2019
 */

public final class PacketOutDisableProperties extends DefaultPacket implements Serializable {

    public PacketOutDisableProperties() {
        super("DisableProperties", new Configuration());
    }
}
