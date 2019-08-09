/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.event.events.player.PlayerConnectEvent;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 14.12.2018
 */

public final class PacketInLoginPlayer implements NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        ReformCloudController.getInstance().getUuid()
            .add(configuration.getValue("uuid", UUID.class));
        ReformCloudController.getInstance().getStatisticsProvider().addLogin();
        ReformCloudController.getInstance().getEventManager()
            .fire(new PlayerConnectEvent(configuration.getValue("uuid", UUID.class)));
    }
}
