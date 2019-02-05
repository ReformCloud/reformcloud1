/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.sync.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 05.02.2019
 */

public final class PacketInSyncScreenUpdate implements Serializable, NetworkInboundHandler {
    private static final long serialVersionUID = -7529295495170339243L;

    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        ReformCloudController.getInstance().getScreenSessionProvider().sendScreenMessage(
                configuration.getStringValue("line"), configuration.getStringValue("from")
        );
    }
}
