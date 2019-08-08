/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.internal.events.CloudServerInfoPreUpdateEvent;
import systems.reformcloud.launcher.BungeecordBootstrap;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public final class PacketInServerInfoUpdate implements NetworkInboundHandler, Serializable {

    @Override
    public void handle(Configuration configuration) {
        ServerInfo serverInfo = configuration.getValue("serverInfo",
            TypeTokenAdaptor.getSERVER_INFO_TYPE());

        final ServerInfo oldInfo =
            ReformCloudAPIBungee.getInstance()
                .getServerInfo(serverInfo.getCloudProcess().getName());

        BungeecordBootstrap.getInstance().getProxy().getPluginManager()
            .callEvent(new CloudServerInfoPreUpdateEvent(oldInfo, serverInfo));
        ReformCloudAPIBungee.getInstance().updateServerInfoInternal(serverInfo);
    }
}
