/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 06.02.2019
 */

public final class PacketInExecuteClientCommand implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        ReformCloudClient.getInstance().getCommandManager()
            .dispatchCommand(configuration.getStringValue("cmd"));
    }
}
