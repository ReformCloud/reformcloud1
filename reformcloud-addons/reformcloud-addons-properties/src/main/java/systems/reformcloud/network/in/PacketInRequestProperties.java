/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import java.io.Serializable;
import java.util.UUID;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.out.PacketOutSendProperties;

/**
 * @author _Klaro | Pasqual K. / created on 22.04.2019
 */

public final class PacketInRequestProperties implements Serializable, NetworkQueryInboundHandler {
    @Override
    public void handle(Configuration configuration, UUID resultID) {
        ReformCloudController.getInstance().getChannelHandler().sendDirectPacket(
                configuration.getStringValue("from"), new PacketOutSendProperties(resultID)
        );
    }
}
