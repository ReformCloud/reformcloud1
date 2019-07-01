/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.sync.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.packet.constants.ChannelConstants;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 06.02.2019
 */

public final class PacketInSyncClientReloadSuccess implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        ReformCloudController.getInstance().getColouredConsoleProvider().info(
            ReformCloudController.getInstance().getLoadedLanguage()
                .getClient_controller_info_reload_success()
                .replace("%name%", configuration.getStringValue("name"))
        );
    }

    @Override
    public long handlingChannel() {
        return ChannelConstants.REFORMCLOUD_SYNC_CLIENT_COMMUNICATION_CHANNEL;
    }
}
