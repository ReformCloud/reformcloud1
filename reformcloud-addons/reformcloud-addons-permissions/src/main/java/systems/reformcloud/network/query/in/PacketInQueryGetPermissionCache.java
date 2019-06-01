/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.in;

import java.io.Serializable;
import java.util.UUID;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.query.out.PacketOutQueryGetPermissionCache;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

public final class PacketInQueryGetPermissionCache implements Serializable, NetworkQueryInboundHandler {
    @Override
    public void handle(Configuration configuration, UUID resultID) {
        ReformCloudController.getInstance().getChannelHandler().sendDirectPacket(configuration.getStringValue("from"),
                new PacketOutQueryGetPermissionCache(resultID));
    }
}
