/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 14.03.2019
 */

public final class PacketOutSendMessage extends Packet implements Serializable {

    public PacketOutSendMessage(UUID uniqueID, String message, String proxy) {
        super("SendPlayerMessage", new Configuration().addValue("uuid", uniqueID)
            .addStringValue("message", message).addStringValue("proxy", proxy)
        );
    }
}
