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

public final class PacketOutUpdateProxyGroupPlugin extends Packet implements Serializable {

    public PacketOutUpdateProxyGroupPlugin(String proxyGroup, String pluginName, String url) {
        super("UpdateProxyGroupPlugin", new Configuration()
            .addStringValue("groupName", proxyGroup)
            .addStringValue("pluginName", pluginName)
            .addStringValue("url", url));
    }
}
