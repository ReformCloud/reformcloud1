/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.event.events.OfflinePlayerUpdateEvent;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 08.03.2019
 */

public final class PacketInUpdateOfflinePlayer implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        OfflinePlayer offlinePlayer = configuration
            .getValue("player", TypeTokenAdaptor.getOFFLINE_PLAYER_TYPE());
        OfflinePlayer before = ReformCloudController.getInstance()
            .getOfflinePlayer(offlinePlayer.getUniqueID());
        OfflinePlayerUpdateEvent offlinePlayerUpdateEvent = new OfflinePlayerUpdateEvent(before,
            offlinePlayer);

        ReformCloudController.getInstance().getEventManager().fire(offlinePlayerUpdateEvent);
        if (offlinePlayerUpdateEvent.isCancelled()) {
            return;
        }

        ReformCloudController.getInstance().getPlayerDatabase().updateOfflinePlayer(offlinePlayer);
    }
}
