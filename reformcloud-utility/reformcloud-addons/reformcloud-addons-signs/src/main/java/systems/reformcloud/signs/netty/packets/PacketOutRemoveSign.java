/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs.netty.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.signs.Sign;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public final class PacketOutRemoveSign extends Packet {
    public PacketOutRemoveSign(final Sign sign) {
        super("RemoveSign", new Configuration().addValue("sign", sign));
    }
}
