/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.sync.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.packets.sync.out.PacketOutSyncUpdateClientInfo;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 15.04.2019
 */

public final class PacketInSyncStandby implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        boolean enable = configuration.getBooleanValue("standby");
        if (enable) {
            ReformCloudClient.getInstance().getClientInfo().setReady(false);
            ReformCloudClient.getInstance().getSynchronizationHandler().setLastInfo(ReformCloudClient.getInstance().getClientInfo());
            ReformCloudClient.getInstance().getChannelHandler().sendDirectPacket(
                    "ReformCloudController", new PacketOutSyncUpdateClientInfo(ReformCloudClient.getInstance().getClientInfo())
            );

            ReformCloudClient.getInstance().getCloudProcessScreenService().getRegisteredServerProcesses().forEach(e -> e.shutdown(true));
            ReformCloudClient.getInstance().getCloudProcessScreenService().getRegisteredProxyProcesses().forEach(e -> e.shutdown(null, true));
        } else {
            ReformCloudClient.getInstance().getClientInfo().setReady(true);
            ReformCloudClient.getInstance().getSynchronizationHandler().setLastInfo(ReformCloudClient.getInstance().getClientInfo());
            ReformCloudClient.getInstance().getChannelHandler().sendDirectPacket(
                    "ReformCloudController", new PacketOutSyncUpdateClientInfo(ReformCloudClient.getInstance().getClientInfo())
            );
        }
    }
}
