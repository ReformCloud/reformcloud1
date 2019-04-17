/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.utility.StringUtil;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 17.04.2019
 */

public final class PacketInQueryPlayerAccepted implements Serializable, NetworkQueryInboundHandler {
    @Override
    public void handle(Configuration configuration, UUID resultID) {
        if (ReformCloudController.getInstance().getUuid().contains(configuration.getValue("uuid", UUID.class)))
            ReformCloudController.getInstance().getChannelHandler().sendDirectPacket(
                    configuration.getStringValue("from"),
                    new Packet(StringUtil.NULL, new Configuration().addBooleanProperty("checked", true))
            );
    }
}
