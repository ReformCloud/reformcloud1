/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.sync.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.out.PacketOutUpdateAll;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 02.02.2019
 */

public final class PacketInSyncUpdateClientInfo implements Serializable, NetworkInboundHandler {
    private static final long serialVersionUID = -8551248371013672598L;

    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        Client client = ReformCloudController.getInstance().getInternalCloudNetwork().getClients().get(configuration.getStringValue("from"));
        if (client == null)
            return;

        client.setClientInfo(configuration.getValue("info", TypeTokenAdaptor.getClientInfoType()));
        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
    }
}
