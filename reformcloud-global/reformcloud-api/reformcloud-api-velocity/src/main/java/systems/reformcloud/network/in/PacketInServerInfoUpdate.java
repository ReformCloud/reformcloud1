/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.TypeTokenAdaptor;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public class PacketInServerInfoUpdate implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudAPIVelocity.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getINTERNAL_CLOUD_NETWORK_TYPE()));
        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork());
    }
}
