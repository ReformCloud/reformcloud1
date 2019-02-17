/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.signaddon.SignSelector;
import systems.reformcloud.utility.TypeTokenAdaptor;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public class PacketInInitializeInternal implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudAPISpigot.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getINTERNAL_CLOUD_NETWORK_TYPE()));
        ReformCloudAPISpigot.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController", new Packet(
                "AuthSuccess", new Configuration().addStringProperty("name", ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess().getName())
        ));

        try {
            new SignSelector();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
