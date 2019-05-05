/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 14.03.2019
 */

public final class PacketOutKickPlayer extends Packet implements Serializable {
    public PacketOutKickPlayer(UUID uuid, String reason) {
        super("KickPlayer", new Configuration().addValue("uuid", uuid).addStringValue("reason", reason));
    }
}
