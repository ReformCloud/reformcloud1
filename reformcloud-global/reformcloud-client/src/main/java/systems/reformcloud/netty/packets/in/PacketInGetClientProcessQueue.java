/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.netty.packets.out.PacketOutClientProcessQueue;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 07.02.2019
 */

public final class PacketInGetClientProcessQueue implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        ReformCloudClient.getInstance().getChannelHandler().sendPacketAsynchronous(
                "ReformCloudController",
                new PacketOutClientProcessQueue(
                        ReformCloudClient.getInstance().getCloudProcessStartupHandler().getServerStartupInfo(),
                        ReformCloudClient.getInstance().getCloudProcessStartupHandler().getProxyStartupInfo()
                )
        );
    }
}
