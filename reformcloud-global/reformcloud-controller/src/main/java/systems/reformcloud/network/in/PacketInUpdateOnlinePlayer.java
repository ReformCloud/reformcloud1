/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.event.events.player.OnlinePlayerUpdateEvent;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.player.implementations.OnlinePlayer;
import systems.reformcloud.utility.TypeTokenAdaptor;
import systems.reformcloud.utility.player.OnlinePlayerManager;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 08.03.2019
 */

public final class PacketInUpdateOnlinePlayer implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        OnlinePlayer onlinePlayer = configuration
            .getValue("player", TypeTokenAdaptor.getONLINE_PLAYER_TYPE());
        OnlinePlayer before = OnlinePlayerManager.INTERNAL_INSTANCE.get(onlinePlayer.getUniqueID());
        OnlinePlayerUpdateEvent onlinePlayerUpdateEvent = new OnlinePlayerUpdateEvent(before,
            onlinePlayer);
        ReformCloudController.getInstance().getEventManager().fire(onlinePlayerUpdateEvent);
        if (onlinePlayerUpdateEvent.isCancelled()) {
            return;
        }

        ReformCloudController.getInstance().updateOnlinePlayer(onlinePlayer);
    }
}
