/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.in;

import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public class PacketInServerInfoUpdate implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        ReformCloudAPIBungee.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getInternalCloudNetworkType()));
        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork());
    }
}
