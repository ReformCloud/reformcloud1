/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;
import systems.reformcloud.player.implementations.OfflinePlayer;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 08.03.2019
 */

public final class PacketOutUpdateOfflinePlayer extends DefaultPacket implements Serializable {

    public PacketOutUpdateOfflinePlayer(OfflinePlayer offlinePlayer) {
        super("UpdateOfflinePlayer", new Configuration().addValue("player", offlinePlayer));
    }
}
