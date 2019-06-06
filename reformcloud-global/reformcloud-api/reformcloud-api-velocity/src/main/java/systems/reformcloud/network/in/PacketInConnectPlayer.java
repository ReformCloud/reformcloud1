/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import com.velocitypowered.api.proxy.Player;
import systems.reformcloud.bootstrap.VelocityBootstrap;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 14.03.2019
 */

public final class PacketInConnectPlayer implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        Player proxiedPlayer = VelocityBootstrap.getInstance().getProxyServer()
            .getPlayer(configuration.getValue("uuid", UUID.class)).orElse(null);
        if (proxiedPlayer == null) {
            return;
        }

        proxiedPlayer.createConnectionRequest(VelocityBootstrap.getInstance().getProxyServer()
            .getServer(configuration.getStringValue("to")).get()).connect();
    }
}
