/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.netty.out.PacketOutServerInfoUpdate;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public class PacketInServerInfoUpdate implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        final ServerInfo serverInfo = configuration.getValue("serverInfo", TypeTokenAdaptor.getServerInfoType());

        ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().updateServerInfo(serverInfo);

        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(ReformCloudController.getInstance().getInternalCloudNetwork());
        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(new PacketOutServerInfoUpdate(serverInfo, ReformCloudController.getInstance().getInternalCloudNetwork()));
    }
}
