/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.query.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.player.DefaultPlayer;
import systems.reformcloud.player.version.SpigotVersion;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 22.02.2019
 */

public final class PacketOutQueryGetPlayer extends Packet implements Serializable {

    public PacketOutQueryGetPlayer(final UUID uuid, final SpigotVersion spigotVersion,
        String name) {
        super("QueryGetPlayer", new Configuration().addValue("player",
            new DefaultPlayer(name, uuid, new HashMap<>(), System.currentTimeMillis(),
                spigotVersion)));
    }

    public PacketOutQueryGetPlayer(final UUID uuid) {
        super("QueryGetPlayer", new Configuration().addValue("uuid", uuid));
    }

    public PacketOutQueryGetPlayer(final String name) {
        super("QueryGetPlayer", new Configuration().addValue("name", name));
    }
}
