/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 29.01.2019
 */

public final class PacketInGetLog implements Serializable, NetworkInboundHandler {

    private static final long serialVersionUID = -2757983690912958355L;

    @Override
    public void handle(Configuration configuration) {
        ReformCloudController.getInstance().getLoggerProvider()
            .info(ReformCloudController.getInstance().getLoadedLanguage().getController_get_log_in()
                .replace("%name%", configuration.getStringValue("process"))
                .replace("%url%", configuration.getStringValue("url")));
    }
}
