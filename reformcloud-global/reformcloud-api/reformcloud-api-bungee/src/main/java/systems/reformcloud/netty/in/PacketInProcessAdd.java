/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.in;

import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.internal.events.CloudProxyAddEvent;
import systems.reformcloud.internal.events.CloudServerAddEvent;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.utility.TypeTokenAdaptor;
import net.md_5.bungee.api.ProxyServer;

import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 07.11.2018
 */

public class PacketInProcessAdd implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        if (configuration.contains("serverInfo")) {
            final ServerInfo serverInfo = configuration.getValue("serverInfo", TypeTokenAdaptor.getServerInfoType());
            final ProxyGroup proxyGroup = ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getProxyGroups().get(ReformCloudAPIBungee.getInstance().getProxyInfo().getProxyGroup().getName());

            if (proxyGroup == null || proxyGroup.getDisabledServerGroups().contains(serverInfo.getServerGroup().getName()))
                return;

            ProxyServer.getInstance().getPluginManager().callEvent(new CloudServerAddEvent(serverInfo));
        } else {
            final ProxyInfo proxyInfo = configuration.getValue("proxyInfo", TypeTokenAdaptor.getProxyInfoType());
            ProxyServer.getInstance().getPluginManager().callEvent(new CloudProxyAddEvent(proxyInfo));
        }
    }
}
