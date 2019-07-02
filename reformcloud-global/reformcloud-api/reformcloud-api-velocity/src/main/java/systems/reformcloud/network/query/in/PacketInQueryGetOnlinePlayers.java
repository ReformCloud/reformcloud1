/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.in;

import java.io.Serializable;
import java.util.UUID;
import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.packets.PacketOutGetOnlinePlayersResult;

/**
 * @author _Klaro | Pasqual K. / created on 02.07.2019
 */

public final class PacketInQueryGetOnlinePlayers implements Serializable,
    NetworkQueryInboundHandler {

    @Override
    public void handle(Configuration configuration, UUID resultID) {
        ReformCloudAPIVelocity.getInstance().getChannelHandler()
            .sendPacketSynchronized(configuration.getStringValue("from"),
                new PacketOutGetOnlinePlayersResult(resultID));
    }
}
