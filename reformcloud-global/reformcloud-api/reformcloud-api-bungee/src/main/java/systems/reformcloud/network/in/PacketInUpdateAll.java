/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 25.11.2018
 */

public final class PacketInUpdateAll implements NetworkInboundHandler, Serializable {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudAPIBungee.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getINTERNAL_CLOUD_NETWORK_TYPE()));
        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getINTERNAL_CLOUD_NETWORK_TYPE()));

        if (ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredProxyByUID(ReformCloudAPIBungee.getInstance().getProxyInfo().getCloudProcess().getProcessUID()) != null)
            ReformCloudAPIBungee.getInstance().setProxyInfo(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredProxyByUID(ReformCloudAPIBungee.getInstance().getProxyInfo().getCloudProcess().getProcessUID()));
    }
}
