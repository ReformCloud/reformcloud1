/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 14.03.2019
 */

public final class PacketOutKickPlayer extends Packet implements Serializable {
    public PacketOutKickPlayer(UUID uniqueID, String reason, String proxy) {
        super("KickPlayer", new Configuration().addProperty("uuid", uniqueID)
                .addStringProperty("reason", reason).addStringProperty("proxy", proxy)
        );
    }
}
