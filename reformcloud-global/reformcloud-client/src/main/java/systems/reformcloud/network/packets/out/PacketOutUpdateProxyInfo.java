/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 25.04.2019
 */

public final class PacketOutUpdateProxyInfo extends Packet implements Serializable {
    public PacketOutUpdateProxyInfo(ProxyInfo proxyInfo) {
        super("ProxyInfoUpdate", new Configuration().addValue("proxyInfo", proxyInfo));
    }
}
