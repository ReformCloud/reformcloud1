/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public final class PacketOutServerInfoUpdate extends Packet {

    public PacketOutServerInfoUpdate(final ServerInfo serverInfo,
        final InternalCloudNetwork internalCloudNetwork) {
        super("ServerInfoUpdate", new Configuration().addValue("serverInfo", serverInfo)
            .addValue("networkProperties", internalCloudNetwork));
    }
}
