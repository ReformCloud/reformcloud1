/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.autoicon.IconManager;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 23.04.2019
 */

public final class PacketInDisableIcons implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        if (IconManager.getInstance() != null) {
            IconManager.getInstance().delete();
        }
    }
}
