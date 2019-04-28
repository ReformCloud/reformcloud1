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
                .addStringValue("type", type)
                .addStringValue("requester", ReformCloudClient.getInstance().getCloudConfiguration().getClientName())
                .addStringValue("groupName", groupName)
                .addStringValue("name", name)
                .addValue("uuid", processUID)
                .addStringValue("template", template));
    }
}
