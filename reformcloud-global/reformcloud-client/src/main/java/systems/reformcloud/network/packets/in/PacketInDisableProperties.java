/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.properties.PropertiesManager;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 22.04.2019
 */

public final class PacketInDisableProperties implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        if (PropertiesManager.available)
            PropertiesManager.getInstance().delete();
    }
}
