/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.serverprocess.startup.CloudServerStartupHandler;
import systems.reformcloud.serverprocess.startup.ProxyStartupHandler;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public class PacketInStopProcess implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        if (ReformCloudClient.getInstance().getCloudProcessScreenService().getRegisteredServerHandler(configuration.getStringValue("name")) != null) {
            final CloudServerStartupHandler cloudServerStartupHandler = ReformCloudClient.getInstance().getCloudProcessScreenService().getRegisteredServerHandler(configuration.getStringValue("name"));
            cloudServerStartupHandler.shutdown(true);
            ReformCloudClient.getInstance().getLoggerProvider().info(ReformCloudClient.getInstance().getInternalCloudNetwork().getLoaded().getClient_shutdown_process()
                    .replace("%name%", cloudServerStartupHandler.getServerStartupInfo().getName()));
        } else if (ReformCloudClient.getInstance().getCloudProcessScreenService().getRegisteredProxyHandler(configuration.getStringValue("name")) != null) {
            final ProxyStartupHandler proxyStartupHandler = ReformCloudClient.getInstance().getCloudProcessScreenService().getRegisteredProxyHandler(configuration.getStringValue("name"));
            proxyStartupHandler.shutdown(null, true);
            ReformCloudClient.getInstance().getLoggerProvider().info(ReformCloudClient.getInstance().getInternalCloudNetwork().getLoaded().getClient_shutdown_process()
                    .replace("%name%", proxyStartupHandler.getProxyStartupInfo().getName()));
        }
    }
}
