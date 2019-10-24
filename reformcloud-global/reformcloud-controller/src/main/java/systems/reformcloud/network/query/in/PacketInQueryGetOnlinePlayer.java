/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.query.out.PacketOutQueryOnlinePlayerResult;
import systems.reformcloud.player.implementations.OnlinePlayer;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 08.03.2019
 */

public final class PacketInQueryGetOnlinePlayer implements Serializable,
    NetworkQueryInboundHandler {

    @Override
    public void handle(Configuration configuration, UUID resultID) {
        if (configuration.contains("uuid")) {
            OnlinePlayer onlinePlayer = ReformCloudController.getInstance().getOnlinePlayer(configuration.getValue("uuid", UUID.class));
            if (onlinePlayer != null) {
                ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                    configuration.getStringValue("from"), new PacketOutQueryOnlinePlayerResult(
                        onlinePlayer, resultID
                    )
                );
            }
        } else {
            UUID uuid = ReformCloudController.getInstance().getPlayerDatabase()
                .getID(configuration.getStringValue("name"));
            if (uuid == null) {
                return;
            }

            OnlinePlayer onlinePlayer = ReformCloudController.getInstance().getOnlinePlayer(uuid);
            if (onlinePlayer != null) {
                ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                    configuration.getStringValue("from"), new PacketOutQueryOnlinePlayerResult(
                        onlinePlayer, resultID
                    )
                );
            }
        }
    }
}
