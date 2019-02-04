/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.in;

import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.launcher.SpigotBootstrap;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.internal.events.CloudServerInfoUpdateEvent;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.utility.TypeTokenAdaptor;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;

import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public class PacketInServerInfoUpdate implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        final ServerInfo serverInfo = configuration.getValue("serverInfo", TypeTokenAdaptor.getServerInfoType());
        final InternalCloudNetwork internalCloudNetwork = configuration.getValue("networkProperties", TypeTokenAdaptor.getInternalCloudNetworkType());

        ReformCloudAPISpigot.getInstance().setInternalCloudNetwork(internalCloudNetwork);
        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(internalCloudNetwork);

        if (serverInfo.getCloudProcess().getName().equals(ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess().getName()))
            ReformCloudAPISpigot.getInstance().setServerInfo(serverInfo);

        SpigotBootstrap.getInstance().getServer().getPluginManager().callEvent(new CloudServerInfoUpdateEvent(serverInfo));
    }
}
