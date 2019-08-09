/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;
import systems.reformcloud.signs.Sign;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public final class PacketOutDeleteSign extends DefaultPacket {

    public PacketOutDeleteSign(final Sign sign) {
        super("RemoveSign", new Configuration().addValue("sign", sign));
    }
}
