/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.sync.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
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
            final ClientInfo clientInfo = client.getClientInfo();
            ReformCloudController.getInstance().getChannelHandler().unregisterChannel(client.getName());
            client.setClientInfo(null);

            clientInfo.getStartedServers().forEach(s -> {
                ServerInfo serverInfo = ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredServerByName(s);
                if (serverInfo != null) {
                    ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().unregisterServerProcess(
                            serverInfo.getCloudProcess().getProcessUID(), serverInfo.getCloudProcess().getName(), serverInfo.getPort()
                    );
                }
            });

            clientInfo.getStartedProxies().forEach(s -> {
                ProxyInfo proxyInfo = ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredProxyByName(s);
                if (proxyInfo != null) {
                    ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().unregisterProxyProcess(
                            proxyInfo.getCloudProcess().getProcessUID(), proxyInfo.getCloudProcess().getName(), proxyInfo.getPort()
                    );
                }
            });
        }
    }
}
