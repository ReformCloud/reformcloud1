package systems.reformcloud.network.packets;

import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

public class PacketOutServerDisable extends Packet {

    public PacketOutServerDisable() {
        super("ServerDisable", new Configuration().addProperty("serverName", ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess().getName()));
    }
}
