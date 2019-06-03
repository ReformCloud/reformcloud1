/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 09.03.2019
 */

public final class PacketOutUpdateServerGroupPluginTemplate extends Packet implements Serializable {

    public PacketOutUpdateServerGroupPluginTemplate(String groupName, String templateName,
        String pluginName, String url) {
        super("UpdateServerGroupPluginTemplate", new Configuration()
            .addStringValue("groupName", groupName)
            .addStringValue("templateName", templateName)
            .addStringValue("pluginName", pluginName)
            .addStringValue("url", url));
    }
}
