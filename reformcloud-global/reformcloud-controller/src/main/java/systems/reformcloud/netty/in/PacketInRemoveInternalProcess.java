/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.netty.out.PacketOutStopProcess;

import java.util.List;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public class PacketInRemoveInternalProcess implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        switch (configuration.getStringValue("type").toLowerCase()) {
            case "proxy": {
                final ProxyInfo proxyInfo = ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredProxyByUID(configuration.getValue("uid", UUID.class));

                if (proxyInfo == null)
                    return;

                ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(proxyInfo.getCloudProcess().getClient(), new PacketOutStopProcess(proxyInfo.getCloudProcess().getName()));
                break;
            }
            case "server": {
                final ServerInfo serverInfo = ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredServerByUID(configuration.getValue("uid", UUID.class));

                if (serverInfo == null)
                    return;

                ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(serverInfo.getCloudProcess().getClient(), new PacketOutStopProcess(serverInfo.getCloudProcess().getName()));
                break;
            }
        }
    }
}
