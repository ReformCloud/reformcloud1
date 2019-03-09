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

public final class PacketOutUpdateServerGroupPlugin extends Packet implements Serializable {
    public PacketOutUpdateServerGroupPlugin(String groupName, String pluginName, String url) {
        super("UpdateServerGroupPlugin", new Configuration()
                .addStringProperty("groupName", groupName)
                .addStringProperty("pluginName", pluginName)
                .addStringProperty("url", url));
    }
}
