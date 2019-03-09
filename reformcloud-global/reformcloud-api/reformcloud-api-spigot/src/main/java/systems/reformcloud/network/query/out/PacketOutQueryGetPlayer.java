/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.out;

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
    public PacketOutQueryGetPlayer(final UUID uuid, final SpigotVersion spigotVersion, String name) {
        super("QueryGetPlayer", new Configuration().addProperty("player",
                new DefaultPlayer(name, uuid, new HashMap<>(), System.currentTimeMillis(), spigotVersion)));
    }

    public PacketOutQueryGetPlayer(final UUID uuid) {
        super("QueryGetPlayer", new Configuration().addProperty("uuid", uuid));
    }

    public PacketOutQueryGetPlayer(final String name) {
        super("QueryGetPlayer", new Configuration().addProperty("name", name));
    }
}
