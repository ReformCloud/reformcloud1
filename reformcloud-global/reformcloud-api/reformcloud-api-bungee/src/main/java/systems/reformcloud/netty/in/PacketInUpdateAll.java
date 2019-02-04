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
 * @author _Klaro | Pasqual K. / created on 25.11.2018
 */

public class PacketInUpdateAll implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        ReformCloudAPIBungee.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getInternalCloudNetworkType()));
        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getInternalCloudNetworkType()));

        if (ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredProxyByUID(ReformCloudAPIBungee.getInstance().getProxyInfo().getCloudProcess().getProcessUID()) != null)
            ReformCloudAPIBungee.getInstance().setProxyInfo(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredProxyByUID(ReformCloudAPIBungee.getInstance().getProxyInfo().getCloudProcess().getProcessUID()));
    }
}
