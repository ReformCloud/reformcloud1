/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.event.events.ServerInfoUpdateEvent;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.out.PacketOutServerInfoUpdate;
import systems.reformcloud.utility.TypeTokenAdaptor;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public final class PacketInServerInfoUpdate implements NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        final ServerInfo serverInfo = configuration
            .getValue("serverInfo", TypeTokenAdaptor.getSERVER_INFO_TYPE());

        ReformCloudController.getInstance().updateServerInfoInternal(serverInfo);
        ReformCloudController.getInstance().getChannelHandler().sendToAllDirect(
            new PacketOutServerInfoUpdate(serverInfo)
        );
        ReformCloudController.getInstance().getEventManager()
            .fire(new ServerInfoUpdateEvent(serverInfo));
    }
}
