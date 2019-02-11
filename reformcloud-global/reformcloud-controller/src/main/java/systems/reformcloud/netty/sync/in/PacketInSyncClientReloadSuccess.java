/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.sync.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 06.02.2019
 */

public final class PacketInSyncClientReloadSuccess implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudController.getInstance().getLoggerProvider().info(
                ReformCloudController.getInstance().getLoadedLanguage().getClient_controller_info_reload_success()
                        .replace("%name%", configuration.getStringValue("name"))
        );
    }
}
