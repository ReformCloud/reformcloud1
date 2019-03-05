/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 11.02.2019
 */

public final class PacketInIconSizeIncorrect implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudController.getInstance().getLoggerProvider().serve("The icon size of proxy " +
                configuration.getStringValue("name") + " is not 64x64. " +
                "Please correct the mistake, otherwise the icon cannot be used");
    }
}
