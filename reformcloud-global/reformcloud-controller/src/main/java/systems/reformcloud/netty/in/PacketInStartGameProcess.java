/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.out.PacketOutStartGameServer;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.util.Collection;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public class PacketInStartGameProcess implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        final ServerGroup serverGroup = configuration.getValue("group", TypeTokenAdaptor.getServerGroupType());
        final Client client = ReformCloudController.getInstance().getBestClient(serverGroup.getClients(), serverGroup.getMemory());

        if (client == null)
            return;

        final Collection<String> servers = ReformCloudController.getInstance().getInternalCloudNetwork()
                .getServerProcessManager().getOnlineServers(serverGroup.getName());
        final Collection<String> waiting = ReformCloudController.getInstance().getCloudProcessOfferService().getWaiting(serverGroup.getName());

        final int waitingAndOnline = servers.size() + waiting.size();
        final String id = Integer.toString(ReformCloudController.getInstance().getCloudProcessOfferService().nextServerID(serverGroup.getName()));
        final String name = serverGroup.getName() + ReformCloudController.getInstance().getCloudConfiguration().getSplitter() + (Integer.parseInt(id) <= 9 ? "0" : "") + id;
        ReformCloudController.getInstance().getCloudProcessOfferService().registerID(serverGroup.getName(), name, Integer.valueOf(id));

        if (serverGroup.getMaxOnline() > waitingAndOnline || serverGroup.getMaxOnline() == -1) {
            if (configuration.contains("template")) {
                ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(client.getName(),
                        new PacketOutStartGameServer(serverGroup, name, UUID.randomUUID(), configuration.getConfiguration("pre"), id, configuration.getStringValue("template"))
                );
            } else {
                ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(client.getName(),
                        new PacketOutStartGameServer(serverGroup, name, UUID.randomUUID(), configuration.getConfiguration("pre"), id)
                );
            }
        }
    }
}
