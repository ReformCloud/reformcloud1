/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.out.PacketOutStopProcess;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public final class PacketInRemoveInternalProcess implements NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        switch (configuration.getStringValue("type").toLowerCase()) {
            case "proxy": {
                final ProxyInfo proxyInfo = ReformCloudController.getInstance()
                    .getInternalCloudNetwork().getServerProcessManager()
                    .getRegisteredProxyByUID(configuration.getValue("uid", UUID.class));

                if (proxyInfo == null) {
                    return;
                }

                ReformCloudController.getInstance().getChannelHandler()
                    .sendPacketAsynchronous(proxyInfo.getCloudProcess().getClient(),
                        new PacketOutStopProcess(proxyInfo.getCloudProcess().getName()));
                break;
            }
            case "server": {
                final ServerInfo serverInfo = ReformCloudController.getInstance()
                    .getInternalCloudNetwork().getServerProcessManager()
                    .getRegisteredServerByUID(configuration.getValue("uid", UUID.class));

                if (serverInfo == null) {
                    return;
                }

                ReformCloudController.getInstance().getChannelHandler()
                    .sendPacketAsynchronous(serverInfo.getCloudProcess().getClient(),
                        new PacketOutStopProcess(serverInfo.getCloudProcess().getName()));
                break;
            }
        }
    }
}
