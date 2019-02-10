/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.netty.out.PacketOutStartProxy;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.sql.Ref;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 26.12.2018
 */

public class PacketInStartProxyProcess implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        final ProxyGroup proxyGroup = configuration.getValue("group", TypeTokenAdaptor.getProxyGroupType());
        final Client client = ReformCloudController.getInstance().getBestClient(proxyGroup.getClients(), proxyGroup.getMemory());

        if (client == null)
            return;

        final Collection<String> proxies = ReformCloudController.getInstance().getInternalCloudNetwork()
                .getServerProcessManager().getOnlineServers(proxyGroup.getName());
        final Collection<String> waiting = ReformCloudController.getInstance().getCloudProcessOfferService().getWaiting(proxyGroup.getName());

        final int waitingAndOnline = proxies.size() + waiting.size();
        final String id = Integer.toString(ReformCloudController.getInstance().getCloudProcessOfferService().nextProxyID(proxyGroup.getName()));
        final String name = proxyGroup.getName() + ReformCloudController.getInstance().getCloudConfiguration().getSplitter() + (Integer.parseInt(id) <= 9 ? "0" : "") + id;
        ReformCloudController.getInstance().getCloudProcessOfferService().registerProxyID(
                proxyGroup.getName(), name, Integer.valueOf(id)
        );

        if (proxyGroup.getMaxOnline() > waitingAndOnline || proxyGroup.getMaxOnline() == -1) {
            if (configuration.contains("template")) {
                ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(client.getName(),
                        new PacketOutStartProxy(proxyGroup, name, UUID.randomUUID(), configuration.getConfiguration("pre"), id, configuration.getStringValue("template"))
                );
            } else {
                ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(client.getName(),
                        new PacketOutStartProxy(proxyGroup, name, UUID.randomUUID(), configuration.getConfiguration("pre"), id)
                );
            }
        }
    }
}
