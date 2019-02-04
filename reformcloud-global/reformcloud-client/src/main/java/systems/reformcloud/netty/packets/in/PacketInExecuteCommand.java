/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public class PacketInExecuteCommand implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        if (configuration.getStringValue("type").equalsIgnoreCase("proxy"))
            ReformCloudClient.getInstance().getCloudProcessScreenService().getRegisteredProxyHandler(configuration.getStringValue("target")).executeCommand(configuration.getStringValue("command"));
        else
            ReformCloudClient.getInstance().getCloudProcessScreenService().getRegisteredServerHandler(configuration.getStringValue("target")).executeCommand(configuration.getStringValue("command"));
    }
}
