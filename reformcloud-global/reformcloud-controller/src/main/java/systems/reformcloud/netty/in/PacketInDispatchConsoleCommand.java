/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public class PacketInDispatchConsoleCommand implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        ReformCloudController.getInstance().getLoggerProvider().warn(ReformCloudController.getInstance().getLoadedLanguage().getController_command_executed_packet());
        ReformCloudController.getInstance().getCommandManager().dispatchCommand(configuration.getStringValue("command"));
    }
}
