/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs.netty.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.signs.SignSelector;
import systems.reformcloud.signs.netty.packets.PacketOutSendSigns;

import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public class PacketInRequestSigns implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(configuration.getStringValue("name"), new PacketOutSendSigns(SignSelector.getInstance().getSignConfiguration().getSignLayoutConfiguration(), SignSelector.getInstance().getSignConfiguration().getSignMap()));
    }
}
