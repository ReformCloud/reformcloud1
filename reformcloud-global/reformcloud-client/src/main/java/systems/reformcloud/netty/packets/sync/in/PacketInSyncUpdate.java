/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets.sync.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 03.02.2019
 */

public final class PacketInSyncUpdate implements Serializable, NetworkInboundHandler {
    private static final long serialVersionUID = -2435825653441303885L;

    @Override
    public void handle(Configuration configuration) {
        ReformCloudClient.getInstance().getCommandManager().dispatchCommand("update confirm");
    }
}
