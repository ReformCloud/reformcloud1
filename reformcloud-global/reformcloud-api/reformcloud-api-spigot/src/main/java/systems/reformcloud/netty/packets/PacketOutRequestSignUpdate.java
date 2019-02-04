/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets;

import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packet.enums.PacketSender;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.util.Collections;

/**
 * @author _Klaro | Pasqual K. / created on 12.01.2019
 */

public final class PacketOutRequestSignUpdate extends Packet {
    public PacketOutRequestSignUpdate() {
        super("RequestSignUpdate", new Configuration().addStringProperty("name", ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess().getName()), Collections.singletonList(QueryType.COMPLETE), PacketSender.PROCESS_SERVER);
    }
}
