/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 23.04.2019
 */

public final class PacketOutEnableIcons extends DefaultPacket implements Serializable {

    public PacketOutEnableIcons() {
        super("EnableIcons", new Configuration());
    }
}
