/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.sync.out;

import java.io.Serializable;
import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.network.packet.constants.ChannelConstants;

/**
 * @author _Klaro | Pasqual K. / created on 03.02.2019
 */

public final class PacketOutSyncClientDisconnects extends Packet implements Serializable {

    private static final long serialVersionUID = -733544534073632717L;

    public PacketOutSyncClientDisconnects() {
        super(
            "ClientDisconnects",
            new Configuration().addStringValue("name",
                ReformCloudClient.getInstance().getCloudConfiguration().getClientName()),
            ChannelConstants.REFORMCLOUD_SYNC_CLIENT_COMMUNICATION_CHANNEL
        );
    }
}
