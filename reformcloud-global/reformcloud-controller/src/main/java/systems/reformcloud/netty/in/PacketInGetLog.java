/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 29.01.2019
 */

public final class PacketInGetLog implements Serializable, NetworkInboundHandler {
    private static final long serialVersionUID = -2757983690912958355L;

    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        ReformCloudController.getInstance().getLoggerProvider().info(ReformCloudController.getInstance().getLoadedLanguage().getController_get_log_in()
                .replace("%name%", configuration.getStringValue("process"))
                .replace("%url%", configuration.getStringValue("url")));
    }
}
