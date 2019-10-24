/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.sync.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;
import systems.reformcloud.network.packet.constants.ChannelConstants;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 05.02.2019
 */

public final class PacketOutSyncScreenUpdate extends DefaultPacket implements Serializable {

    private static final long serialVersionUID = 3205044307706658304L;

    public PacketOutSyncScreenUpdate(final String line, final String who) {
        super(
            "ScreenUpdate",
            new Configuration()
                .addStringValue("line", line)
                .addStringValue("from", who),
            ChannelConstants.REFORMCLOUD_SYNC_CLIENT_COMMUNICATION_CHANNEL
        );
    }
}
