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
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public final class PacketInServerInfoUpdate implements NetworkInboundHandler, Serializable {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudAPIBungee.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getINTERNAL_CLOUD_NETWORK_TYPE()));
        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork());
    }
}
