/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.out.PacketOutStopProcess;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 25.04.2019
 */

public final class PacketInStopProcess implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        String name = configuration.getStringValue("name");
        ServerInfo serverInfo = ReformCloudController.getInstance().getServerInfo(name);
        if (serverInfo != null) {
            ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                serverInfo.getCloudProcess().getClient(), new PacketOutStopProcess(name)
            );
        } else {
            ProxyInfo proxyInfo = ReformCloudController.getInstance().getProxyInfo(name);
            if (proxyInfo != null) {
                ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                    proxyInfo.getCloudProcess().getClient(), new PacketOutStopProcess(name)
                );
            }
        }
    }
}
