/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.launcher.BungeecordBootstrap;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.utility.StringUtil;

/**
 * @author _Klaro | Pasqual K. / created on 02.07.2019
 */

public final class PacketOutGetOnlinePlayersResult extends Packet implements Serializable {

    public PacketOutGetOnlinePlayersResult(UUID result) {
        super(StringUtil.NULL, new Configuration().addValue("players",
            convert()), result);
    }

    private static List<UUID> convert() {
        List<UUID> out = new LinkedList<>();
        BungeecordBootstrap.getInstance().getProxy().getPlayers()
            .forEach(e -> out.add(e.getUniqueId()));
        return out;
    }

}
