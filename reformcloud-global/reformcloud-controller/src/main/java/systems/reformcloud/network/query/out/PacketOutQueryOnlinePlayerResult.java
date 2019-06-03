/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.player.implementations.OnlinePlayer;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 08.03.2019
 */

public final class PacketOutQueryOnlinePlayerResult extends Packet implements Serializable {

    public PacketOutQueryOnlinePlayerResult(OnlinePlayer onlinePlayer, UUID resultID) {
        super("undefined", new Configuration().addValue("result", onlinePlayer));
        super.setResult(resultID);
    }
}
