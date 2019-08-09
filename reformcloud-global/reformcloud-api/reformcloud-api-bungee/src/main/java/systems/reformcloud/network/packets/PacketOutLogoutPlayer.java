/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 14.12.2018
 */

public final class PacketOutLogoutPlayer extends DefaultPacket {

    public PacketOutLogoutPlayer(final UUID uuid) {
        super("LogoutPlayer", new Configuration().addValue("uuid", uuid));
    }
}
