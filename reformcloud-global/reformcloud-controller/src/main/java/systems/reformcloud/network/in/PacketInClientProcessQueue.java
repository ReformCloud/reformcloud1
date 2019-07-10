/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.logging.ColouredConsoleProvider;
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
        final ColouredConsoleProvider colouredConsoleProvider = ReformCloudController.getInstance()
            .getColouredConsoleProvider();
        colouredConsoleProvider
            .info("ProcessQueue of Client §3" + configuration.getStringValue("name") + ":");

        Queue<ServerStartupInfo> servers = configuration
            .getValue("servers", new TypeToken<Queue<ServerStartupInfo>>() {
            }.getType());
        Queue<ProxyStartupInfo> proxies = configuration
            .getValue("proxies", new TypeToken<Queue<ProxyStartupInfo>>() {
            }.getType());

        colouredConsoleProvider.info(" - ServerProcesses: ");
        if (servers.isEmpty()) {
            colouredConsoleProvider.info("    There are §c0§r server processes in the §3Client§r queue");
        } else {
            servers.forEach(e -> colouredConsoleProvider.info(
                "    - §e" + e.getName() + "§r | Group: §e" + e.getServerGroup().getName() + "§r | UID: §e"
                    + e.getUid()));
        }

        colouredConsoleProvider.emptyLine();

        colouredConsoleProvider.info(" - ProxyProcesses: ");
        if (proxies.isEmpty()) {
            colouredConsoleProvider.info("    There are §c0§r proxy processes in the §3Client§r queue");
        } else {
            proxies.forEach(e -> colouredConsoleProvider.info(
                "    - §e" + e.getName() + "§r | Group: §e" + e.getProxyGroup().getName() + "§r | UID: §e" + e
                    .getUid()));
        }
    }
}
