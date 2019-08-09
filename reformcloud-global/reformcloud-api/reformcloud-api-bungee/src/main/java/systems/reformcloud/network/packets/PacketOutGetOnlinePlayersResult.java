/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.launcher.BungeecordBootstrap;
import systems.reformcloud.network.packet.DefaultPacket;
import systems.reformcloud.utility.StringUtil;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 02.07.2019
 */

public final class PacketOutGetOnlinePlayersResult extends DefaultPacket implements Serializable {

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
