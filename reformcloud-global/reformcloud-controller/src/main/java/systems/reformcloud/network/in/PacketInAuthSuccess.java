/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

public final class PacketInAuthSuccess implements NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        ReformCloudController.getInstance().getColouredConsoleProvider().info(
            ReformCloudController.getInstance().getLoadedLanguage().getController_process_ready()
                .replace("%name%", configuration.getStringValue("name")));
    }
}
