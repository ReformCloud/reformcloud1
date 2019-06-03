/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.packets.out.PacketOutClientProcessQueue;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 07.02.2019
 */

public final class PacketInGetClientProcessQueue implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        ReformCloudClient.getInstance().getChannelHandler().sendPacketAsynchronous(
            "ReformCloudController",
            new PacketOutClientProcessQueue(
                ReformCloudClient.getInstance().getCloudProcessStartupHandler()
                    .getServerStartupInfo(),
                ReformCloudClient.getInstance().getCloudProcessStartupHandler()
                    .getProxyStartupInfo()
            )
        );
    }
}
