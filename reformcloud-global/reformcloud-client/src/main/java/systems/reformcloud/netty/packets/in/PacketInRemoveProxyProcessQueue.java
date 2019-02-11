/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 07.02.2019
 */

public final class PacketInRemoveProxyProcessQueue implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudClient.getInstance().getCloudProcessStartupHandler().removeProxyProcess(configuration.getStringValue("name"));
    }
}
