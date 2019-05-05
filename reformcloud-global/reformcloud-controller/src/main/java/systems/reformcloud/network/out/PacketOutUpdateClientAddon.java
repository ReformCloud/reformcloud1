/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 09.03.2019
 */

public final class PacketOutUpdateClientAddon extends Packet implements Serializable {
    public PacketOutUpdateClientAddon(String name, String url) {
        super("UpdateClientAddon", new Configuration()
                .addStringValue("name", name)
                .addStringValue("url", url));
    }
}
