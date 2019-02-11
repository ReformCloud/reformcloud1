/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.sync.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.serverprocess.startup.CloudServerStartupHandler;
import systems.reformcloud.serverprocess.startup.ProxyStartupHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 05.02.2019
 */

public final class PacketInSyncScreenJoin implements Serializable, NetworkInboundHandler {
    private static final long serialVersionUID = 7400484794437983879L;

    @Override
    public void handle(Configuration configuration) {
        if (configuration.getStringValue("name").equalsIgnoreCase(ReformCloudClient.getInstance().getCloudConfiguration().getClientName())) {
            ReformCloudClient.getInstance().getClientScreenHandler().enableScreen();
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
