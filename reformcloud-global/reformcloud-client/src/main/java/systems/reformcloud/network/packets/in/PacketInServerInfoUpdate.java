/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.TypeTokenAdaptor;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public final class PacketInServerInfoUpdate implements NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        ServerInfo serverInfo = configuration.getValue("serverInfo", TypeTokenAdaptor.getSERVER_INFO_TYPE());
        ReformCloudClient.getInstance().updateServerInfoInternal(serverInfo);
    }
}
