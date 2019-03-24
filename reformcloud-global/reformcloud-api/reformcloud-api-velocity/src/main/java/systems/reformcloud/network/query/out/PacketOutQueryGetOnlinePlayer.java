/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 08.03.2019
 */

public final class PacketOutQueryGetOnlinePlayer extends Packet implements Serializable {
    public PacketOutQueryGetOnlinePlayer(UUID uuid) {
        super("QueryGetOnlinePlayer", new Configuration().addProperty("uuid", uuid));
    }

    public PacketOutQueryGetOnlinePlayer(String name) {
        super("QueryGetOnlinePlayer", new Configuration().addProperty("name", name));
    }
}
