/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 08.03.2019
 */

public final class PacketOutQueryGetOnlinePlayer extends DefaultPacket implements Serializable {

    public PacketOutQueryGetOnlinePlayer(UUID uuid) {
        super("QueryGetOnlinePlayer", new Configuration().addValue("uuid", uuid));
    }

    public PacketOutQueryGetOnlinePlayer(String name) {
        super("QueryGetOnlinePlayer", new Configuration().addValue("name", name));
    }
}
