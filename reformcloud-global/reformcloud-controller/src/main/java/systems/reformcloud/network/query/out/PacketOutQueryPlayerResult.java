/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.player.DefaultPlayer;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 22.02.2019
 */

public final class PacketOutQueryPlayerResult extends Packet implements Serializable {
    public PacketOutQueryPlayerResult(DefaultPlayer defaultPlayer, UUID result) {
        super("undefined", new Configuration().addProperty("result", defaultPlayer));
        this.setResult(result);
    }
}
