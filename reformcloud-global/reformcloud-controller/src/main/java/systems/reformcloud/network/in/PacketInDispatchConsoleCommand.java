/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public class PacketInDispatchConsoleCommand implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudController.getInstance().getLoggerProvider().warn(ReformCloudController.getInstance().getLoadedLanguage().getController_command_executed_packet());
        ReformCloudController.getInstance().getCommandManager().dispatchCommand(configuration.getStringValue("command"));
    }
}
