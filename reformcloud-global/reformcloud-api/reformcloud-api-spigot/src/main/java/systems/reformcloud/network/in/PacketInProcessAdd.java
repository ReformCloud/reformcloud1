/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.internal.events.CloudProxyAddEvent;
import systems.reformcloud.internal.events.CloudServerAddEvent;
import systems.reformcloud.launcher.SpigotBootstrap;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 07.11.2018
 */

public final class PacketInProcessAdd implements NetworkInboundHandler, Serializable {
    @Override
    public void handle(Configuration configuration) {
        if (configuration.contains("serverInfo")) {
            final ServerInfo serverInfo = configuration.getValue("serverInfo", TypeTokenAdaptor.getSERVER_INFO_TYPE());
            SpigotBootstrap.getInstance().getServer().getPluginManager().callEvent(new CloudServerAddEvent(serverInfo));
        } else {
            final ProxyInfo proxyInfo = configuration.getValue("proxyInfo", TypeTokenAdaptor.getPROXY_INFO_TYPE());
            SpigotBootstrap.getInstance().getServer().getPluginManager().callEvent(new CloudProxyAddEvent(proxyInfo));
        }
    }
}
