/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets.sync.out;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packet.enums.PacketSender;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.io.Serializable;
import java.util.Collections;

/**
 * @author _Klaro | Pasqual K. / created on 02.02.2019
 */

public final class PacketOutSyncUpdateClientInfo extends Packet implements Serializable {
    private static final long serialVersionUID = -1798278215718848219L;

    public PacketOutSyncUpdateClientInfo(final ClientInfo clientInfo) {
        super(
                "UpdateClientInfo",
                new Configuration()
                        .addStringProperty("from", ReformCloudClient.getInstance().getCloudConfiguration().getClientName())
                        .addProperty("info", clientInfo),
                Collections.singletonList(QueryType.COMPLETE),
                PacketSender.CLIENT
        );
    }
}
