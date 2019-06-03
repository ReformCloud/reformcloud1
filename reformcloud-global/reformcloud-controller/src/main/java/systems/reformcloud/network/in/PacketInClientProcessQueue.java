/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.meta.startup.ProxyStartupInfo;
import systems.reformcloud.meta.startup.ServerStartupInfo;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.io.Serializable;
import java.util.Queue;

/**
 * @author _Klaro | Pasqual K. / created on 07.02.2019
 */

public final class PacketInClientProcessQueue implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        final LoggerProvider loggerProvider = ReformCloudController.getInstance()
            .getLoggerProvider();
        loggerProvider
            .info("ProcessQueue of Client §3" + configuration.getStringValue("name") + ":");

        Queue<ServerStartupInfo> servers = configuration
            .getValue("servers", new TypeToken<Queue<ServerStartupInfo>>() {
            }.getType());
        Queue<ProxyStartupInfo> proxies = configuration
            .getValue("proxies", new TypeToken<Queue<ProxyStartupInfo>>() {
            }.getType());

        loggerProvider.info(" - ServerProcesses: ");
        if (servers.isEmpty()) {
            loggerProvider.info("    There are §c0§r server processes in the §3Client§r queue");
        } else {
            servers.forEach(e -> loggerProvider.info(
                "    - " + e.getName() + " | Group: " + e.getServerGroup().getName() + " | UID: "
                    + e.getUid()));
        }

        loggerProvider.emptyLine();

        loggerProvider.info(" - ProxyProcesses: ");
        if (proxies.isEmpty()) {
            loggerProvider.info("    There are §c0§r proxy processes in the §3Client§r queue");
        } else {
            proxies.forEach(e -> loggerProvider.info(
                "    - " + e.getName() + " | Group: " + e.getProxyGroup().getName() + " | UID: " + e
                    .getUid()));
        }
    }
}
