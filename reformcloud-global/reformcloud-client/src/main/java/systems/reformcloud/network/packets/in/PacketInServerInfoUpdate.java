/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.TypeTokenAdaptor;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public final class PacketInServerInfoUpdate implements NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        final InternalCloudNetwork internalCloudNetwork = configuration
            .getValue("networkProperties", TypeTokenAdaptor.getINTERNAL_CLOUD_NETWORK_TYPE());

        ReformCloudClient.getInstance().setInternalCloudNetwork(internalCloudNetwork);
        ReformCloudLibraryServiceProvider.getInstance()
            .setInternalCloudNetwork(internalCloudNetwork);
    }
}
