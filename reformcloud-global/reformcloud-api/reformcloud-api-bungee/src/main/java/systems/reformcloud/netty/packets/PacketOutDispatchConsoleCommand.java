/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packet.enums.PacketSender;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.util.Collections;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public final class PacketOutDispatchConsoleCommand extends Packet {
    public PacketOutDispatchConsoleCommand(final String command) {
        super("DispatchCommandLine", new Configuration().addProperty("command", command), Collections.singletonList(QueryType.COMPLETE), PacketSender.PROCESS_PROXY);
    }
}
