/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 14.12.2018
 */

public final class PacketOutLoginPlayer extends Packet {

    public PacketOutLoginPlayer(final UUID uuid) {
        super("LoginPlayer", new Configuration().addValue("uuid", uuid));
    }
}
