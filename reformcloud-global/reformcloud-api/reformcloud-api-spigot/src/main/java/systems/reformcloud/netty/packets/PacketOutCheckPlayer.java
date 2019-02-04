/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets;

import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packet.enums.PacketSender;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.util.Collections;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 14.12.2018
 */

public final class PacketOutCheckPlayer extends Packet {

    public PacketOutCheckPlayer(final UUID uuid) {
        super("PlayerAccepted", new Configuration().addStringProperty("name", ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess().getName()).addProperty("uuid", uuid), Collections.singletonList(QueryType.COMPLETE), PacketSender.PROCESS_SERVER);
    }
}
