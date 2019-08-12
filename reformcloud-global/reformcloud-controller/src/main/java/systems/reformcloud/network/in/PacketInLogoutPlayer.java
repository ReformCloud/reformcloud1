/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.event.events.player.PlayerDisconnectsEvent;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.player.OnlinePlayerManager;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 14.12.2018
 */

public final class PacketInLogoutPlayer implements NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        ReformCloudController.getInstance().getUuid()
            .remove(configuration.getValue("uuid", UUID.class));
        OnlinePlayerManager.INTERNAL_INSTANCE.logoutPlayer(configuration.getValue("uuid", UUID.class));
        ReformCloudController.getInstance().getEventManager()
            .fire(new PlayerDisconnectsEvent(configuration.getValue("uuid", UUID.class)));
    }
}
