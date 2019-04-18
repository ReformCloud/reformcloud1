/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.network.packets.out.PacketOutRequestParameters;
import systems.reformcloud.network.packets.sync.out.PacketOutSyncUpdateClientInfo;
import systems.reformcloud.parameters.ParameterGroup;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

public final class PacketInInitializeInternal implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudClient.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getINTERNAL_CLOUD_NETWORK_TYPE()));
        ReformCloudClient.getInstance().getLoggerProvider().info("NetworkProperties are now set and ReformCloudClient is now ready");

        ReformCloudClient.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController", new PacketOutSyncUpdateClientInfo(ReformCloudClient.getInstance().getClientInfo()));

        ReformCloudClient.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController", new Packet(
                "AuthSuccess",
                new Configuration().addStringProperty("name", ReformCloudClient.getInstance().getCloudConfiguration().getClientName())
        ));

        ReformCloudClient.getInstance().getChannelHandler().sendPacketQuerySync(
                "ReformCloudController",
                ReformCloudClient.getInstance().getCloudConfiguration().getClientName(),
                new PacketOutRequestParameters(),
                (configuration1, resultID) -> ReformCloudClient.getInstance().getParameterManager().update(
                        configuration1.getValue("parameters", new TypeToken<List<ParameterGroup>>() {
                        }.getType())),
                (configuration2, resultId) -> {
                }
        );
    }
}