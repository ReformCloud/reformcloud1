/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs.netty.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.signs.SignSelector;
import systems.reformcloud.signs.netty.packets.PacketOutSendSigns;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public final class PacketInRequestSigns implements NetworkQueryInboundHandler {
    @Override
    public void handle(Configuration configuration, UUID resultID) {
        ReformCloudController.getInstance().getChannelHandler()
                .sendDirectPacket(configuration.getStringValue("from"),
                        new PacketOutSendSigns(SignSelector.getInstance().getSignConfiguration()
                                .getSignLayoutConfiguration(), SignSelector.getInstance().getSignConfiguration().getSignMap(), resultID));
    }
}
