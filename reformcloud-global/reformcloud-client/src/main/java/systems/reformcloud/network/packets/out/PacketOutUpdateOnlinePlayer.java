/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;
import systems.reformcloud.player.implementations.OnlinePlayer;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 08.03.2019
 */

public final class PacketOutUpdateOnlinePlayer extends DefaultPacket implements Serializable {

    public PacketOutUpdateOnlinePlayer(OnlinePlayer onlinePlayer) {
        super("UpdateOnlinePlayer", new Configuration().addValue("player", onlinePlayer));
    }
}
