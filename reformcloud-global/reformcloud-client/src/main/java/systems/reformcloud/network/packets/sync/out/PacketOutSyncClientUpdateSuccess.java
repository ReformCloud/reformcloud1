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
 * @author _Klaro | Pasqual K. / created on 06.02.2019
 */

public final class PacketOutSyncClientUpdateSuccess extends Packet implements Serializable {

    private static final long serialVersionUID = -3306501769364526001L;

    public PacketOutSyncClientUpdateSuccess() {
        super(
            "ClientReloadSuccess",
            new Configuration().addStringValue("name",
                ReformCloudClient.getInstance().getCloudConfiguration().getClientName()),
            ChannelConstants.REFORMCLOUD_SYNC_CLIENT_COMMUNICATION_CHANNEL
        );
    }
}
