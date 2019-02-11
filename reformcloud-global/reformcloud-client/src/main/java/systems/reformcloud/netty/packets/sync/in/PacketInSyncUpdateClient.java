/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets.sync.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 06.02.2019
 */

public final class PacketInSyncUpdateClient implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudClient.getInstance().reloadAll();
    }
}
