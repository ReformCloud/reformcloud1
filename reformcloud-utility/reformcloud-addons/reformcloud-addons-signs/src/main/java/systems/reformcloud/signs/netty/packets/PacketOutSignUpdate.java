/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs.netty.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.signs.Sign;
import systems.reformcloud.signs.SignLayoutConfiguration;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 19.04.2019
 */

public final class PacketOutSignUpdate extends Packet implements Serializable {
    public PacketOutSignUpdate(final SignLayoutConfiguration signLayoutConfiguration, final Map<UUID, Sign> signs) {
        super("SignUpdate", new Configuration().addValue("signConfig", signLayoutConfiguration).addValue("signMap", signs));
    }
}
