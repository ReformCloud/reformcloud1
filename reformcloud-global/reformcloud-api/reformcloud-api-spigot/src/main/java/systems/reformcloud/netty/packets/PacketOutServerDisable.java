package systems.reformcloud.netty.packets;

import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packet.enums.PacketSender;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.util.Collections;

public class PacketOutServerDisable extends Packet {

    public PacketOutServerDisable() {
        super("ServerDisable", new Configuration().addProperty("serverName", ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess().getName()), Collections.singletonList(QueryType.COMPLETE), PacketSender.PROCESS_SERVER);
    }
}
