/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 15.04.2019
 */

public final class PacketInEnableDebug implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider()
            .setDebug(configuration.getBooleanValue("enabled"));
    }
}
