/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.startup.ServerStartupInfo;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

public final class PacketInStartGameServer implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        final ServerGroup serverGroup = configuration.getValue("group", TypeTokenAdaptor.getSERVER_GROUP_TYPE());

        ReformCloudClient.getInstance().getCloudProcessStartupHandler().offerServerProcess(new ServerStartupInfo(
                configuration.getValue("serverProcess", UUID.class),
                configuration.getStringValue("name"),
                (configuration.contains("template") ? configuration.getStringValue("template") : null),
                serverGroup,
                configuration.getConfiguration("preConfig"),
                configuration.getIntegerValue("id")
        ));
    }
}
