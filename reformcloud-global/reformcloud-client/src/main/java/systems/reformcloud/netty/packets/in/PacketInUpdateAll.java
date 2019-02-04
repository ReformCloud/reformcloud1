/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 25.11.2018
 */

public class PacketInUpdateAll implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        ReformCloudClient.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getInternalCloudNetworkType()));
        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getInternalCloudNetworkType()));
    }
}
