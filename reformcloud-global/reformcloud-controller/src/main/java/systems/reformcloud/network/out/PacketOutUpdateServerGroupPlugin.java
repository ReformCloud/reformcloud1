/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 09.03.2019
 */

public final class PacketOutUpdateServerGroupPlugin extends DefaultPacket implements Serializable {

    public PacketOutUpdateServerGroupPlugin(String groupName, String pluginName, String url) {
        super("UpdateServerGroupPlugin", new Configuration()
            .addStringValue("groupName", groupName)
            .addStringValue("pluginName", pluginName)
            .addStringValue("url", url));
    }
}
