/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.Packet;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 14.12.2018
 */

public class PacketInPlayerAccepted implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(configuration.getStringValue("name"),
                new Packet("PlayerAccepted", new Configuration().addBooleanProperty("accepted", ReformCloudController.getInstance().getUuid().contains(configuration.getValue("uuid", UUID.class))).addProperty("uuid", configuration.getValue("uuid", UUID.class))));
    }
}
