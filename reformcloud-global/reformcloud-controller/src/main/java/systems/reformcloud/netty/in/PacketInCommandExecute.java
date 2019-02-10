/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.util.List;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 30.11.2018
 */

public class PacketInCommandExecute implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        ReformCloudController.getInstance().getLoggerProvider().info(ReformCloudController.getInstance().getLoadedLanguage().getController_command_executed()
                .replace("%name%", configuration.getStringValue("name"))
                .replace("%uuid%", String.valueOf(configuration.getValue("uuid", UUID.class)))
                .replace("%proxy%", configuration.getStringValue("proxyName"))
                .replace("%command%", configuration.getStringValue("command"))
                .replace("%server%", configuration.getStringValue("server")));
        ReformCloudController.getInstance().getStatisticsProvider().addIngameCommand();
    }
}
