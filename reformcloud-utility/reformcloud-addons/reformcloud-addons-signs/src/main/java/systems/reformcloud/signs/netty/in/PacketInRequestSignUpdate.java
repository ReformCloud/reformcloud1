/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs.netty.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.signs.SignSelector;
import systems.reformcloud.signs.netty.packets.PacketOutSendSigns;

/**
 * @author _Klaro | Pasqual K. / created on 12.01.2019
 */

public class PacketInRequestSignUpdate implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(
                configuration.getStringValue("name"),
                new PacketOutSendSigns(
                        SignSelector
                                .getInstance()
                                .getSignConfiguration()
                                .getSignLayoutConfiguration(),
                        SignSelector
                                .getInstance()
                                .getSignConfiguration()
                                .getSignMap())
        );
    }
}
