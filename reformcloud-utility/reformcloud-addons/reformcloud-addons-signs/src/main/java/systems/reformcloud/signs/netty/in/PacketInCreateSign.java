/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs.netty.in;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.signs.Sign;
import systems.reformcloud.signs.SignSelector;
import systems.reformcloud.signs.netty.packets.PacketOutCreateSign;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public class PacketInCreateSign implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        SignSelector.getInstance().getSignConfiguration().addSign(configuration.getValue("sign", new TypeToken<Sign>() {
        }.getType()));
        ReformCloudController.getInstance().getChannelHandler().sendToAllAsynchronous(new PacketOutCreateSign(configuration.getValue("sign", new TypeToken<Sign>() {
        }.getType())));
    }
}
