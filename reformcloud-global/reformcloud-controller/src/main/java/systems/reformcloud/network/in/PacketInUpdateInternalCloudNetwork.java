/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.event.events.update.InternalCloudNetworkUpdateEvent;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.out.PacketOutUpdateAll;
import systems.reformcloud.utility.TypeTokenAdaptor;

/**
 * @author _Klaro | Pasqual K. / created on 30.10.2018
 */

public final class PacketInUpdateInternalCloudNetwork implements NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        ReformCloudController.getInstance().setInternalCloudNetwork(configuration
            .getValue("networkProperties", TypeTokenAdaptor.getINTERNAL_CLOUD_NETWORK_TYPE()));
        ReformCloudLibraryServiceProvider.getInstance()
            .setInternalCloudNetwork(ReformCloudController.getInstance().getInternalCloudNetwork());

        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(
            new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
        ReformCloudController.getInstance().getEventManager()
            .fire(new InternalCloudNetworkUpdateEvent(
                ReformCloudController.getInstance().getInternalCloudNetwork())
            );
    }
}
