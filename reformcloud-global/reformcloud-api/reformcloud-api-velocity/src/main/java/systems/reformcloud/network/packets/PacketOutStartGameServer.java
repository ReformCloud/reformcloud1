/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.network.packet.DefaultPacket;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public final class PacketOutStartGameServer extends DefaultPacket {

    public PacketOutStartGameServer(final ServerGroup serverGroup,
        final Configuration preConfiguration) {
        super("StartGameProcess", new Configuration().addValue("group", serverGroup)
            .addConfigurationValue("pre", preConfiguration));
    }

    public PacketOutStartGameServer(final ServerGroup serverGroup,
        final Configuration preConfiguration, final String template) {
        super("StartGameProcess",
            new Configuration().addValue("group", serverGroup).addStringValue("template", template)
                .addConfigurationValue("pre", preConfiguration));
    }
}
