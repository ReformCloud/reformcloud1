/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.TypeTokenAdaptor;

/**
 * @author _Klaro | Pasqual K. / created on 25.11.2018
 */

public final class PacketInUpdateAll implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudClient.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getInternalCloudNetworkType()));
        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getInternalCloudNetworkType()));
    }
}
