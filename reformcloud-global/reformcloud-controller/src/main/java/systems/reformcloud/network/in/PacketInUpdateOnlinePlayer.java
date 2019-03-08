/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.player.implementations.OnlinePlayer;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 08.03.2019
 */

public final class PacketInUpdateOnlinePlayer implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        OnlinePlayer onlinePlayer = configuration.getValue("player", TypeTokenAdaptor.getONLINE_PLAYER_TYPE());
        ReformCloudController.getInstance().getPlayerDatabase().updateOnlinePlayer(onlinePlayer);
    }
}
