/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.launcher.BungeecordBootstrap;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 14.03.2019
 */

public final class PacketInConnectPlayer implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        ProxiedPlayer proxiedPlayer = BungeecordBootstrap.getInstance().getProxy()
            .getPlayer(configuration.getValue("uuid", UUID.class));
        if (proxiedPlayer == null) {
            return;
        }

        proxiedPlayer.connect(BungeecordBootstrap.getInstance().getProxy()
            .getServerInfo(configuration.getStringValue("to")));
    }
}
