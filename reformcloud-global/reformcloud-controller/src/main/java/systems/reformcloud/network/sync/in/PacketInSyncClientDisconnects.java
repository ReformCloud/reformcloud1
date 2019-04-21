/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.sync.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 03.02.2019
 */

public final class PacketInSyncClientDisconnects implements Serializable, NetworkInboundHandler {
    private static final long serialVersionUID = -6533784184811381053L;

    @Override
    public void handle(Configuration configuration) {
        Client client = ReformCloudController.getInstance().getInternalCloudNetwork().getClients().get(configuration.getStringValue("name"));
        if (client != null) {
            client.setClientInfo(null);
            ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getAllRegisteredServerProcesses().forEach(e -> {
                if (e.getCloudProcess().getClient().equals(client.getName())) {
                    ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().unregisterServerProcess(
                            e.getCloudProcess().getProcessUID(), e.getCloudProcess().getName(), e.getPort()
                    );
                    ReformCloudController.getInstance().getCloudProcessOfferService().unregisterID(e);
                }

                if (ReformCloudController.getInstance().getScreenSessionProvider().isInScreen(e.getCloudProcess().getName()))
                    ReformCloudController.getInstance().getScreenSessionProvider().leaveScreen();
            });

            ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getAllRegisteredProxyProcesses().forEach(e -> {
                if (e.getCloudProcess().getClient().equals(client.getName())) {
                    ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().unregisterProxyProcess(
                            e.getCloudProcess().getProcessUID(), e.getCloudProcess().getName(), e.getPort()
                    );
                    ReformCloudController.getInstance().getCloudProcessOfferService().unregisterProxyID(e);
                }

                if (ReformCloudController.getInstance().getScreenSessionProvider().isInScreen(e.getCloudProcess().getName()))
                    ReformCloudController.getInstance().getScreenSessionProvider().leaveScreen();
            });
        }
    }
}
