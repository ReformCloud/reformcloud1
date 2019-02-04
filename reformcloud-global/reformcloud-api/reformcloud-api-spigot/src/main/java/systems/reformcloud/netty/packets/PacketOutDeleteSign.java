/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packet.enums.PacketSender;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.signs.Sign;

import java.util.Collections;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public final class PacketOutDeleteSign extends Packet {
    public PacketOutDeleteSign(final Sign sign) {
        super("RemoveSign", new Configuration().addProperty("sign", sign), Collections.singletonList(QueryType.COMPLETE), PacketSender.PROCESS_SERVER);
    }
}
