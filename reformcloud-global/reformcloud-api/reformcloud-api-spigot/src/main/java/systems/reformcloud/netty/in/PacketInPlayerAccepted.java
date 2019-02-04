/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.in;

import systems.reformcloud.launcher.SpigotBootstrap;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.util.List;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 14.12.2018
 */

public class PacketInPlayerAccepted implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        if (configuration.getBooleanValue("accepted")) {
            SpigotBootstrap.getInstance().getAcceptedPlayers().add(configuration.getValue("uuid", UUID.class));
            SpigotBootstrap.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(SpigotBootstrap.getInstance(), () -> {
                SpigotBootstrap.getInstance().getAcceptedPlayers().remove(configuration.getValue("uuid", UUID.class));
            }, 40L);
        }
    }
}
