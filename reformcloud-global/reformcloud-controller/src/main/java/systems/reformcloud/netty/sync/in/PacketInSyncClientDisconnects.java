/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.sync.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 03.02.2019
 */

public final class PacketInSyncClientDisconnects implements Serializable, NetworkInboundHandler {
    private static final long serialVersionUID = -6533784184811381053L;

    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        Client client = ReformCloudController.getInstance().getInternalCloudNetwork().getClients().get(configuration.getStringValue("name"));
        if (client != null) {
            client.getClientInfo().getStartedServers().forEach(s -> {
                ServerInfo serverInfo = ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredServerByName(s);
                if (serverInfo != null) {
                    ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().unregisterServerProcess(
                            serverInfo.getCloudProcess().getProcessUID(), serverInfo.getCloudProcess().getName(), serverInfo.getPort()
                    );
                }
            });

            client.getClientInfo().getStartedProxies().forEach(s -> {
                ProxyInfo proxyInfo = ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredProxyByName(s);
                if (proxyInfo != null) {
                    ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().unregisterServerProcess(
                            proxyInfo.getCloudProcess().getProcessUID(), proxyInfo.getCloudProcess().getName(), proxyInfo.getPort()
                    );
                }
            });
            client.setClientInfo(null);
        }
    }
}
