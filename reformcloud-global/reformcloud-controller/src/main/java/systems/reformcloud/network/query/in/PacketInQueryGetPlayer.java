/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.query.out.PacketOutQueryPlayerResult;
import systems.reformcloud.player.DefaultPlayer;
import systems.reformcloud.player.version.SpigotVersion;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 22.02.2019
 */

public final class PacketInQueryGetPlayer implements Serializable, NetworkQueryInboundHandler {
    @Override
    public void handle(Configuration configuration, UUID resultID) {
        ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(configuration.getStringValue("from"),
                new PacketOutQueryPlayerResult(new DefaultPlayer("_Klaro", UUID.randomUUID(), SpigotVersion.V1_8_8), resultID));
    }
}
