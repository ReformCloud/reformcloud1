/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 14.03.2019
 */

public final class PacketOutConnectPlayer extends DefaultPacket implements Serializable {

    public PacketOutConnectPlayer(UUID uniqueID, String to, String proxy) {
        super("ConnectPlayer", new Configuration().addValue("uuid", uniqueID)
            .addStringValue("to", to).addStringValue("proxy", proxy)
        );
    }
}
