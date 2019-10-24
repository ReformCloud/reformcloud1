/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 11.02.2019
 */

public final class PacketOutIconSizeIncorrect extends DefaultPacket implements Serializable {

    public PacketOutIconSizeIncorrect(final String name) {
        super("IconSizeIncorrect", new Configuration().addStringValue("proxy", name));
    }
}
