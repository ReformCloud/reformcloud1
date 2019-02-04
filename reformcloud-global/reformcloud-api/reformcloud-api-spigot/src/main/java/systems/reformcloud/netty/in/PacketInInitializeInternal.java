/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.in;

import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packet.enums.PacketSender;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.signaddon.SignSelector;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.util.Collections;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public class PacketInInitializeInternal implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        if (!queryTypes.contains(QueryType.RESULT) || queryTypes.contains(QueryType.NO_RESULT)) return;

        ReformCloudAPISpigot.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getInternalCloudNetworkType()));
        ReformCloudAPISpigot.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController", new Packet(
                "AuthSuccess", new Configuration().addStringProperty("name", ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess().getName()),
                Collections.singletonList(QueryType.COMPLETE), PacketSender.PROCESS_SERVER
        ));

        try {
            new SignSelector();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
