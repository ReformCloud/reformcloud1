/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets.sync.out;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packet.enums.PacketSender;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.io.Serializable;
import java.util.Collections;

/**
 * @author _Klaro | Pasqual K. / created on 06.02.2019
 */

public final class PacketOutSyncClientUpdateSuccess extends Packet implements Serializable {
    private static final long serialVersionUID = -3306501769364526001L;

    public PacketOutSyncClientUpdateSuccess() {
        super(
                "ClientReloadSuccess",
                new Configuration().addStringProperty("name", ReformCloudClient.getInstance().getCloudConfiguration().getClientName()),
                Collections.singletonList(QueryType.COMPLETE),
                PacketSender.CLIENT
        );
    }
}
