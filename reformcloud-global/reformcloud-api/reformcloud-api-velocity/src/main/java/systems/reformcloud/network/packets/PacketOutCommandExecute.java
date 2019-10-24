/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 07.11.2018
 */

public final class PacketOutCommandExecute extends DefaultPacket {

    public PacketOutCommandExecute(final String name, final UUID uuid, final String command,
        final String server) {
        super("CommandExecute", new Configuration()
            .addValue("uuid", uuid)
            .addStringValue("command", command)
            .addStringValue("name", name)
            .addStringValue("server", server)
            .addStringValue("proxyName",
                ReformCloudAPIVelocity.getInstance().getProxyInfo().getCloudProcess().getName()));
    }
}
