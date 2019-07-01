/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.sync.in;

import java.io.Serializable;
import java.util.UUID;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.packet.constants.ChannelConstants;
import systems.reformcloud.network.sync.out.PacketOutSyncNameToUUID;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.uuid.UUIDConverter;

/**
 * @author _Klaro | Pasqual K. / created on 16.02.2019
 */

public final class PacketInSyncNameToUUID implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        final String name = configuration.getStringValue("requester");
        if (!name.equals(StringUtil.NULL)) {
            final UUID uuid = UUIDConverter.getUUIDFromName(configuration.getStringValue("player"));
            if (uuid != null) {
                ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(name,
                    new PacketOutSyncNameToUUID(uuid, configuration.getStringValue("player")));
            }
        }
    }

    @Override
    public long handlingChannel() {
        return ChannelConstants.REFORMCLOUD_SYNC_CLIENT_COMMUNICATION_CHANNEL;
    }
}
