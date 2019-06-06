/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.network.packet.Packet;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 28.10.2018
 */

public final class PacketOutStartGameServer extends Packet {

    public PacketOutStartGameServer(final ServerGroup group, final String processName,
        final UUID serverProcess, final Configuration configuration, final String id) {
        super("StartCloudServer",
            new Configuration().addValue("group", group).addStringValue("name", processName)
                .addValue("serverProcess", serverProcess)
                .addConfigurationValue("preConfig", configuration)
                .addIntegerValue("id", Integer.valueOf(id))
        );
        ReformCloudController.getInstance().getLoggerProvider().info(
            ReformCloudController.getInstance().getLoadedLanguage()
                .getController_server_added_to_queue()
                .replace("%uid%", String.valueOf(serverProcess))
                .replace("%name%", processName));
    }

    public PacketOutStartGameServer(final ServerGroup group, final String processName,
        final UUID serverProcess, final Configuration configuration, final String id,
        final String template) {
        super("StartCloudServer",
            new Configuration().addValue("group", group).addStringValue("name", processName)
                .addStringValue("template", template).addValue("serverProcess", serverProcess)
                .addConfigurationValue("preConfig", configuration)
                .addIntegerValue("id", Integer.valueOf(id))
        );
        ReformCloudController.getInstance().getLoggerProvider().info(
            ReformCloudController.getInstance().getLoadedLanguage()
                .getController_server_added_to_queue()
                .replace("%uid%", String.valueOf(serverProcess))
                .replace("%name%", processName));
    }
}
