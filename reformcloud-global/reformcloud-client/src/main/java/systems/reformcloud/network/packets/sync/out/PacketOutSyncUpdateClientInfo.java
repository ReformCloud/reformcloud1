/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.sync.out;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.network.packet.DefaultPacket;
import systems.reformcloud.network.packet.constants.ChannelConstants;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 02.02.2019
 */

public final class PacketOutSyncUpdateClientInfo extends DefaultPacket implements Serializable {

    private static final long serialVersionUID = -1798278215718848219L;

    public PacketOutSyncUpdateClientInfo(final ClientInfo clientInfo) {
        super(
            "UpdateClientInfo",
            new Configuration()
                .addStringValue("from",
                    ReformCloudClient.getInstance().getCloudConfiguration().getClientName())
                .addValue("info", clientInfo),
            ChannelConstants.REFORMCLOUD_SYNC_CLIENT_COMMUNICATION_CHANNEL
        );
    }
}
