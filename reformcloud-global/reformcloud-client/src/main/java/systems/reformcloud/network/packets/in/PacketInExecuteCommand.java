/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public final class PacketInExecuteCommand implements NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        if (configuration.getStringValue("type").equalsIgnoreCase("proxy")) {
            ReformCloudClient.getInstance().getCloudProcessScreenService()
                .getRegisteredProxyHandler(configuration.getStringValue("target"))
                .executeCommand(configuration.getStringValue("command"));
        } else {
            ReformCloudClient.getInstance().getCloudProcessScreenService()
                .getRegisteredServerHandler(configuration.getStringValue("target"))
                .executeCommand(configuration.getStringValue("command"));
        }
    }
}
