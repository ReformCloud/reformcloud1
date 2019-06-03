/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.out.PacketOutStartProxy;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.util.Collection;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 26.12.2018
 */

public final class PacketInStartProxyProcess implements NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        final ProxyGroup proxyGroup = configuration
            .getValue("group", TypeTokenAdaptor.getPROXY_GROUP_TYPE());
        final Client client = ReformCloudController.getInstance()
            .getBestClient(proxyGroup.getClients(), proxyGroup.getMemory());

        if (client == null) {
            return;
        }

        final Collection<String> proxies = ReformCloudController.getInstance()
            .getInternalCloudNetwork()
            .getServerProcessManager().getOnlineServers(proxyGroup.getName());
        final Collection<String> waiting = ReformCloudController.getInstance()
            .getCloudProcessOfferService().getWaiting(proxyGroup.getName());

        final int waitingAndOnline = proxies.size() + waiting.size();
        final String id = Integer.toString(
            ReformCloudController.getInstance().getCloudProcessOfferService()
                .nextProxyID(proxyGroup.getName()));
        final String name =
            proxyGroup.getName() + ReformCloudController.getInstance().getCloudConfiguration()
                .getSplitter() + (Integer.parseInt(id) <= 9 ? "0" : "") + id;
        ReformCloudController.getInstance().getCloudProcessOfferService().registerProxyID(
            proxyGroup.getName(), name, Integer.valueOf(id)
        );

        if (proxyGroup.getMaxOnline() > waitingAndOnline || proxyGroup.getMaxOnline() == -1) {
            if (configuration.contains("template")) {
                ReformCloudController.getInstance().getChannelHandler()
                    .sendPacketAsynchronous(client.getName(),
                        new PacketOutStartProxy(proxyGroup, name, UUID.randomUUID(),
                            configuration.getConfiguration("pre"), id,
                            configuration.getStringValue("template"))
                    );
            } else {
                ReformCloudController.getInstance().getChannelHandler()
                    .sendPacketAsynchronous(client.getName(),
                        new PacketOutStartProxy(proxyGroup, name, UUID.randomUUID(),
                            configuration.getConfiguration("pre"), id)
                    );
            }
        }
    }
}
