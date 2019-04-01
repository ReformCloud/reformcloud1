/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.packet.Packet;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 14.12.2018
 */

public final class PacketInPlayerAccepted implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(configuration.getStringValue("name"),
                new Packet("PlayerAccepted", new Configuration()
                        .addBooleanProperty("accepted", ReformCloudController.getInstance().getUuid()
                                .contains(configuration.getValue("uuid", UUID.class)))
                        .addProperty("uuid", configuration.getValue("uuid", UUID.class))));
    }
}
