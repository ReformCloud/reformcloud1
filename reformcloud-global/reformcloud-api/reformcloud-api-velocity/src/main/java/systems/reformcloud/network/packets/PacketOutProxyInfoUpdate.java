/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.network.packet.Packet;

/**
 * @author _Klaro | Pasqual K. / created on 06.12.2018
 */

public final class PacketOutProxyInfoUpdate extends Packet {
    public PacketOutProxyInfoUpdate(final ProxyInfo proxyInfo) {
        super("ProxyInfoUpdate", new Configuration().addProperty("proxyInfo", proxyInfo));
    }
}
