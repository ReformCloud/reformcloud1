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
 * @author _Klaro | Pasqual K. / created on 11.02.2019
 */

public final class PacketOutSyncExceptionThrown extends Packet implements Serializable {

    public PacketOutSyncExceptionThrown(final Throwable cause) {
        super("ExceptionThrown", new Configuration()
            .addStringValue("name",
                ReformCloudClient.getInstance().getCloudConfiguration().getClientName())
                .addValue("cause", cause),
            ChannelConstants.REFORMCLOUD_SYNC_CLIENT_COMMUNICATION_CHANNEL);
    }
}
