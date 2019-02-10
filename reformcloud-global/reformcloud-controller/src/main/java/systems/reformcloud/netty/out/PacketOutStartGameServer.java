/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.out;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packet.enums.PacketSender;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 28.10.2018
 */

public final class PacketOutStartGameServer extends Packet {
    public PacketOutStartGameServer(final ServerGroup group, final String processName, final UUID serverProcess, final Configuration configuration, final String id) {
        super("StartCloudServer",
                new Configuration().addProperty("group", group).addStringProperty("name", processName).addProperty("serverProcess", serverProcess).addConfigurationProperty("preConfig", configuration).addIntegerProperty("id", Integer.valueOf(id)),
                Arrays.asList(QueryType.COMPLETE, QueryType.NO_RESULT), PacketSender.CONTROLLER);
        ReformCloudController.getInstance().getLoggerProvider().info(ReformCloudController.getInstance().getLoadedLanguage().getController_server_added_to_queue()
                .replace("%uid%", String.valueOf(serverProcess))
                .replace("%name%", processName));
    }

    public PacketOutStartGameServer(final ServerGroup group, final String processName, final UUID serverProcess, final Configuration configuration, final String id, final String template) {
        super("StartCloudServer",
                new Configuration().addProperty("group", group).addStringProperty("name", processName).addStringProperty("template", template).addProperty("serverProcess", serverProcess).addConfigurationProperty("preConfig", configuration).addIntegerProperty("id", Integer.valueOf(id)),
                Arrays.asList(QueryType.COMPLETE, QueryType.NO_RESULT), PacketSender.CONTROLLER);
        ReformCloudController.getInstance().getLoggerProvider().info(ReformCloudController.getInstance().getLoadedLanguage().getController_server_added_to_queue()
                .replace("%uid%", String.valueOf(serverProcess))
                .replace("%name%", processName));
    }
}
