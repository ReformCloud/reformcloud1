/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import org.bukkit.Bukkit;
import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.internal.events.CloudServerInfoUpdateEvent;
import systems.reformcloud.internal.events.ServerInfoPreUpdateEvent;
import systems.reformcloud.launcher.SpigotBootstrap;
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
        final ServerInfo serverInfo = configuration
            .getValue("serverInfo", TypeTokenAdaptor.getSERVER_INFO_TYPE());

        final ServerInfo oldInfo =
            ReformCloudAPISpigot.getInstance()
                .getServerInfo(serverInfo.getCloudProcess().getName());
        Bukkit.getPluginManager().callEvent(new ServerInfoPreUpdateEvent(oldInfo, serverInfo));

        ReformCloudAPISpigot.getInstance().updateServerInfoInternal(serverInfo);

        if (serverInfo.getCloudProcess().getName().equals(
                ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess().getName())) {
            ReformCloudAPISpigot.getInstance().setServerInfo(serverInfo);
        }

        SpigotBootstrap.getInstance().getServer().getPluginManager()
            .callEvent(new CloudServerInfoUpdateEvent(serverInfo));
    }
}
