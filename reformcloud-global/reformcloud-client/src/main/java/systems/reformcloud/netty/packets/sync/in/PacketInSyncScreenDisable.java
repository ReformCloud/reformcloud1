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

public final class PacketInSyncScreenDisable implements Serializable, NetworkInboundHandler {
    private static final long serialVersionUID = -918967605207956872L;

    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        CloudServerStartupHandler cloudServerStartupHandler = ReformCloudClient.getInstance()
                .getCloudProcessScreenService()
                .getRegisteredServerHandler(configuration.getStringValue("name"));
        if (cloudServerStartupHandler != null) {
            cloudServerStartupHandler.getScreenHandler().disableScreen();
        } else {
            ProxyStartupHandler proxyStartupHandler = ReformCloudClient.getInstance()
                    .getCloudProcessScreenService()
                    .getRegisteredProxyHandler(configuration.getStringValue("name"));
            if (proxyStartupHandler != null)
                proxyStartupHandler.getScreenHandler().disableScreen();
        }
    }
}
