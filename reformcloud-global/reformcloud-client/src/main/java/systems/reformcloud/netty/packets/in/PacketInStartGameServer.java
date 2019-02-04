/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.startup.ServerStartupInfo;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.util.List;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

public class PacketInStartGameServer implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        final ServerGroup serverGroup = configuration.getValue("group", TypeTokenAdaptor.getServerGroupType());

        ReformCloudClient.getInstance().getCloudProcessStartupHandler().offerServerProcess(new ServerStartupInfo(
                configuration.getValue("serverProcess", UUID.class), configuration.getStringValue("name"), (configuration.contains("template") ? configuration.getStringValue("template") : null), serverGroup, configuration.getConfiguration("preConfig"), configuration.getIntegerValue("id")
        ));
    }
}
