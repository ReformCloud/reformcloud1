/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.sync.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.event.events.process.ProxyStoppedEvent;
import systems.reformcloud.event.events.process.ServerStoppedEvent;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.out.PacketOutUpdateAll;
import systems.reformcloud.network.packet.constants.ChannelConstants;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author _Klaro | Pasqual K. / created on 03.02.2019
 */

public final class PacketInSyncClientDisconnects implements Serializable, NetworkInboundHandler {

    private static final long serialVersionUID = -6533784184811381053L;

    @Override
    public void handle(Configuration configuration) {
        Client client = ReformCloudController.getInstance().getInternalCloudNetwork().getClients()
            .get(configuration.getStringValue("name"));
        if (client != null) {
            client.setClientInfo(null);
            Collection<String> waitingOnClient = new LinkedList<>();
            ReformCloudController.getInstance().getCloudProcessOfferService().getWaitingPerClient()
                .forEach((k, v) -> {
                    if (v.equals(client.getName())) {
                        waitingOnClient.add(k);
                    }
                });

            waitingOnClient.forEach(e -> {
                ReformCloudController.getInstance().getCloudProcessOfferService()
                    .removeWaitingProcess(e);
                ReformCloudController.getInstance().getCloudProcessOfferService()
                    .getWaitingPerClient().remove(e);
            });

            ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager()
                .getAllRegisteredServerProcesses().forEach(e -> {
                if (e.getCloudProcess().getClient().equals(client.getName())) {
                    ReformCloudController.getInstance().getInternalCloudNetwork()
                        .getServerProcessManager().unregisterServerProcess(
                        e.getCloudProcess().getProcessUID(), e.getCloudProcess().getName()
                    );
                    ReformCloudController.getInstance().getCloudProcessOfferService()
                        .unregisterID(e);
                    ReformCloudController.getInstance().getEventManager()
                        .fire(new ServerStoppedEvent(e));
                }

                if (ReformCloudController.getInstance().getScreenSessionProvider()
                    .isInScreen(e.getCloudProcess().getName())) {
                    ReformCloudController.getInstance().getScreenSessionProvider().leaveScreen();
                }
            });

            ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager()
                .getAllRegisteredProxyProcesses().forEach(e -> {
                if (e.getCloudProcess().getClient().equals(client.getName())) {
                    ReformCloudController.getInstance().getInternalCloudNetwork()
                        .getServerProcessManager().unregisterProxyProcess(
                        e.getCloudProcess().getProcessUID(), e.getCloudProcess().getName()
                    );
                    ReformCloudController.getInstance().getCloudProcessOfferService()
                        .unregisterProxyID(e);
                    ReformCloudController.getInstance().getEventManager()
                        .fire(new ProxyStoppedEvent(e));
                }

                if (ReformCloudController.getInstance().getScreenSessionProvider()
                    .isInScreen(e.getCloudProcess().getName())) {
                    ReformCloudController.getInstance().getScreenSessionProvider().leaveScreen();
                }
            });

            ReformCloudController.getInstance().getChannelHandler().sendToAllDirect(
                new PacketOutUpdateAll(
                    ReformCloudController.getInstance().getInternalCloudNetwork()));
        }
    }

    @Override
    public long handlingChannel() {
        return ChannelConstants.REFORMCLOUD_SYNC_CLIENT_COMMUNICATION_CHANNEL;
    }
}
