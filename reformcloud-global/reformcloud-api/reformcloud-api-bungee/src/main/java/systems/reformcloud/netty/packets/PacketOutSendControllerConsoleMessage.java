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
 * @author _Klaro | Pasqual K. / created on 30.11.2018
 */

public final class PacketOutSendControllerConsoleMessage extends Packet {
    public PacketOutSendControllerConsoleMessage(final String message) {
        super("SendControllerConsoleMessage", new Configuration().addStringProperty("message", message), Collections.singletonList(QueryType.COMPLETE), PacketSender.PROCESS_PROXY);
    }

    public PacketOutSendControllerConsoleMessage(final String... messages) {
        for (String message : messages)
            new PacketOutSendControllerConsoleMessage(message);
    }
}
