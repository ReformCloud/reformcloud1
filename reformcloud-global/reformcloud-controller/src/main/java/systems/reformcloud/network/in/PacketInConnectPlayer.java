/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.out.PacketOutConnectPlayer;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 14.03.2019
 */

public final class PacketInConnectPlayer implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
            configuration.getStringValue("proxy"),
            new PacketOutConnectPlayer(
                configuration.getValue("uuid", UUID.class),
                configuration.getStringValue("to")
            )
        );
    }
}
