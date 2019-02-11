/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.in;

import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.02.2019
 */

public final class PacketInSyncControllerTime implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudAPISpigot.getInstance().setInternalTime(configuration.getLongValue("time"));
    }
}
