/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.launcher.SpigotBootstrap;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 14.12.2018
 */

public class PacketInPlayerAccepted implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        if (configuration.getBooleanValue("accepted")) {
            SpigotBootstrap.getInstance().getAcceptedPlayers().add(configuration.getValue("uuid", UUID.class));
            SpigotBootstrap.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(SpigotBootstrap.getInstance(), () -> {
                SpigotBootstrap.getInstance().getAcceptedPlayers().remove(configuration.getValue("uuid", UUID.class));
            }, 40L);
        }
    }
}
