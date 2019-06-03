/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import net.md_5.bungee.api.ProxyServer;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.internal.events.CloudProxyRemoveEvent;
import systems.reformcloud.internal.events.CloudServerRemoveEvent;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

public final class PacketInProcessRemove implements NetworkInboundHandler, Serializable {

    @Override
    public void handle(Configuration configuration) {
        if (configuration.contains("serverInfo")) {
            final ServerInfo serverInfo = configuration
                .getValue("serverInfo", TypeTokenAdaptor.getSERVER_INFO_TYPE());
            ProxyServer.getInstance().getPluginManager()
                .callEvent(new CloudServerRemoveEvent(serverInfo));
        } else {
            final ProxyInfo proxyInfo = configuration
                .getValue("proxyInfo", TypeTokenAdaptor.getPROXY_INFO_TYPE());
            ProxyServer.getInstance().getPluginManager()
                .callEvent(new CloudProxyRemoveEvent(proxyInfo));
        }
    }
}
