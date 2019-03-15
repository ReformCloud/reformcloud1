/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.out;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 15.03.2019
 */

public final class PacketOutGetControllerTemplate extends Packet implements Serializable {
    public PacketOutGetControllerTemplate(String type, String groupName, String template, UUID processUID, String name) {
        super("GetControllerTemplate", new Configuration()
                .addStringProperty("type", type)
                .addStringProperty("requester", ReformCloudClient.getInstance().getCloudConfiguration().getClientName())
                .addStringProperty("groupName", groupName)
                .addStringProperty("name", name)
                .addProperty("uuid", processUID)
                .addStringProperty("template", template));
    }
}
