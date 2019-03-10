/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.network.packet.Packet;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public final class PacketOutStartGameServer extends Packet {
    public PacketOutStartGameServer(final ServerGroup serverGroup, final Configuration preConfiguration) {
        super("StartGameProcess", new Configuration().addProperty("group", serverGroup).addConfigurationProperty("pre", preConfiguration));
    }

    public PacketOutStartGameServer(final ServerGroup serverGroup, final Configuration preConfiguration, final String template) {
        super("StartGameProcess", new Configuration().addProperty("group", serverGroup).addStringProperty("template", template).addConfigurationProperty("pre", preConfiguration));
    }
}
