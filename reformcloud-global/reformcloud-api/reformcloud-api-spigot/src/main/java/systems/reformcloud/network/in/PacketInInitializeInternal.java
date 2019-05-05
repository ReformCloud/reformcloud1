/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.enums.ServerState;
import systems.reformcloud.mobsaddon.MobSelector;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.network.packets.PacketOutServerInfoUpdate;
import systems.reformcloud.network.query.out.PacketOutQueryGetPermissionCache;
import systems.reformcloud.signaddon.SignSelector;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public final class PacketInInitializeInternal implements NetworkInboundHandler, Serializable {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudAPISpigot.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getINTERNAL_CLOUD_NETWORK_TYPE()));
        ReformCloudAPISpigot.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController", new Packet(
                "AuthSuccess", new Configuration().addStringValue("name", ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess().getName())
        ));

        if (ReformCloudAPISpigot.getInstance().getServerInfo().getServerGroup().getServerModeType().equals(ServerModeType.LOBBY)) {
            try {
                new SignSelector();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            new MobSelector();
        }

        ReformCloudAPISpigot.getInstance().getServerInfo().setServerState(ServerState.READY);
        ReformCloudAPISpigot.getInstance().sendPacketSync("ReformCloudController", new PacketOutServerInfoUpdate(
                ReformCloudAPISpigot.getInstance().getServerInfo()
        ));

        ReformCloudAPISpigot.getInstance().sendPacketQuery("ReformCloudController",
                new PacketOutQueryGetPermissionCache(), (configuration1, resultID) ->
                        ReformCloudAPISpigot.getInstance().setPermissionCache(configuration1.getValue("cache",
                                TypeTokenAdaptor.getPERMISSION_CACHE_TYPE())
                        )
        );
    }
}
