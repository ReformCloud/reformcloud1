/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.sync.in;

import java.io.Serializable;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.packet.constants.ChannelConstants;

/**
 * @author _Klaro | Pasqual K. / created on 05.02.2019
 */

public final class PacketInSyncScreenUpdate implements Serializable, NetworkInboundHandler {

    private static final long serialVersionUID = -7529295495170339243L;

    @Override
    public void handle(Configuration configuration) {
        ReformCloudController.getInstance().getScreenSessionProvider().sendScreenMessage(
            configuration.getStringValue("line"), configuration.getStringValue("from")
        );
    }

    @Override
    public long handlingChannel() {
        return ChannelConstants.REFORMCLOUD_SYNC_CLIENT_COMMUNICATION_CHANNEL;
    }
}
