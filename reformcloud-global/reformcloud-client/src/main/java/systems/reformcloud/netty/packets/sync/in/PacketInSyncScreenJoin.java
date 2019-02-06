/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets.sync.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.serverprocess.startup.CloudServerStartupHandler;
import systems.reformcloud.serverprocess.startup.ProxyStartupHandler;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 05.02.2019
 */

public final class PacketInSyncScreenJoin implements Serializable, NetworkInboundHandler {
    private static final long serialVersionUID = 7400484794437983879L;

    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        if (configuration.getStringValue("name").equalsIgnoreCase(ReformCloudClient.getInstance().getCloudConfiguration().getClientName())) {
            ReformCloudClient.getInstance().getClientScreenHandler().getScreenHandler().enableScreen();
            return;
        }

        CloudServerStartupHandler cloudServerStartupHandler = ReformCloudClient.getInstance()
                .getCloudProcessScreenService()
                .getRegisteredServerHandler(configuration.getStringValue("name"));
        if (cloudServerStartupHandler != null) {
            cloudServerStartupHandler.getScreenHandler().enableScreen();
        } else {
            ProxyStartupHandler proxyStartupHandler = ReformCloudClient.getInstance()
                    .getCloudProcessScreenService()
                    .getRegisteredProxyHandler(configuration.getStringValue("name"));
            if (proxyStartupHandler != null) {
                proxyStartupHandler.getScreenHandler().enableScreen();
            }
        }
    }
}
