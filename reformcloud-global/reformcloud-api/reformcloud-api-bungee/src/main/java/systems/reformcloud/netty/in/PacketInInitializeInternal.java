/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.in;

import net.md_5.bungee.api.ProxyServer;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.net.InetSocketAddress;

/**
 * @author _Klaro | Pasqual K. / created on 02.11.2018
 */

public class PacketInInitializeInternal implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudAPIBungee.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getInternalCloudNetworkType()));

        final ProxyGroup proxyGroup = ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getProxyGroups().get(ReformCloudAPIBungee.getInstance().getProxyInfo().getProxyGroup().getName());
        if (proxyGroup == null)
            return;

        ReformCloudAPIBungee.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController", new Packet(
                "AuthSuccess", new Configuration().addStringProperty("name", ReformCloudAPIBungee.getInstance().getProxyInfo().getCloudProcess().getName())
        ));
        ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getServerProcessManager().getAllRegisteredServerProcesses().forEach(
                process -> {
                    if (proxyGroup.getDisabledServerGroups().contains(process.getServerGroup().getName()))
                        return;

                    ProxyServer.getInstance().getServers().put(
                            process.getCloudProcess().getName(),
                            ProxyServer.getInstance().constructServerInfo(
                                    process.getCloudProcess().getName(),
                                    new InetSocketAddress(process.getHost(), process.getPort()),
                                    "ReformCloudServer",
                                    false
                            ));

                    if (process.getServerGroup().getServerModeType().equals(ServerModeType.LOBBY)) {
                        ProxyServer.getInstance().getConfig().getListeners().forEach(listener ->
                                listener.getServerPriority().add(process.getCloudProcess().getName())
                        );
                    }
                }
        );
    }
}
