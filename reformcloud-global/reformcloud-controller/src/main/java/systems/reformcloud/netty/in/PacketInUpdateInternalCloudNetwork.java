/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.netty.out.PacketOutUpdateAll;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 30.10.2018
 */

public class PacketInUpdateInternalCloudNetwork implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        ReformCloudController.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getInternalCloudNetworkType()));
        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(ReformCloudController.getInstance().getInternalCloudNetwork());

        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
    }
}
