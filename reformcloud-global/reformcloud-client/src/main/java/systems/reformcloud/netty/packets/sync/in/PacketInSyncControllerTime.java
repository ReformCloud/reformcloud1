/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets.sync.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 10.02.2019
 */

public final class PacketInSyncControllerTime implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        ReformCloudClient.getInstance().setInternalTime(configuration.getLongValue("time"));
    }
}
