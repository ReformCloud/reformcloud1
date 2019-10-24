/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.packet.DefaultPacket;

/**
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

public final class PacketOutProcessAdd extends DefaultPacket {

    public PacketOutProcessAdd(final ServerInfo serverInfo) {
        super("ProcessAdd", new Configuration().addValue("serverInfo", serverInfo));
    }

    public PacketOutProcessAdd(final ProxyInfo proxyInfo) {
        super("ProcessAdd", new Configuration().addValue("proxyInfo", proxyInfo));
    }
}
