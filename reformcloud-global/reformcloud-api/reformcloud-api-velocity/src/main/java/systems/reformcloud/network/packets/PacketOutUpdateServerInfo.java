/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 25.04.2019
 */

public final class PacketOutUpdateServerInfo extends Packet implements Serializable {
    public PacketOutUpdateServerInfo(ServerInfo serverInfo) {
        super("ServerInfoUpdate", new Configuration().addValue("serverInfo", serverInfo));
    }
}
