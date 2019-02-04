/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.in;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.internal.events.CloudProxyRemoveEvent;
import systems.reformcloud.internal.events.CloudServerRemoveEvent;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.utility.TypeTokenAdaptor;
import net.md_5.bungee.api.ProxyServer;

import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

public class PacketInProcessRemove implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        if (configuration.contains("serverInfo")) {
            final ServerInfo serverInfo = configuration.getValue("serverInfo", TypeTokenAdaptor.getServerInfoType());
            ProxyServer.getInstance().getPluginManager().callEvent(new CloudServerRemoveEvent(serverInfo));
        } else {
            final ProxyInfo proxyInfo = configuration.getValue("proxyInfo", TypeTokenAdaptor.getProxyInfoType());
            ProxyServer.getInstance().getPluginManager().callEvent(new CloudProxyRemoveEvent(proxyInfo));
        }
    }
}
