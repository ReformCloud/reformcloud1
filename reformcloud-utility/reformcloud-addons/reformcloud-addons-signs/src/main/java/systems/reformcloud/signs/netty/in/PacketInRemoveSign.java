/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs.netty.in;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.signs.Sign;
import systems.reformcloud.signs.SignSelector;
import systems.reformcloud.signs.netty.packets.PacketOutRemoveSign;

import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public class PacketInRemoveSign implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        SignSelector.getInstance().getSignConfiguration().removeSign(configuration.getValue("sign", new TypeToken<Sign>() {
        }.getType()));
        ReformCloudController.getInstance().getChannelHandler().sendToAllAsynchronous(new PacketOutRemoveSign(configuration.getValue("sign", new TypeToken<Sign>() {
        }.getType())));
    }
}
