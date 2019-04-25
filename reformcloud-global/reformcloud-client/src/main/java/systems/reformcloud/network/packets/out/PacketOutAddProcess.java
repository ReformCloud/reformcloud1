/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.packet.Packet;

/**
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

public final class PacketOutAddProcess extends Packet {
    public PacketOutAddProcess(final ServerInfo serverInfo) {
        super("ProcessAdd", new Configuration().addValue("serverInfo", serverInfo));
    }

    public PacketOutAddProcess(final ProxyInfo proxyInfo) {
        super("ProcessAdd", new Configuration().addValue("proxyInfo", proxyInfo));
    }
}
