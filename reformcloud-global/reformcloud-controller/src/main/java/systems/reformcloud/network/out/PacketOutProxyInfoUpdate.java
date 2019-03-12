/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 12.03.2019
 */

public final class PacketOutProxyInfoUpdate extends Packet implements Serializable {
    public PacketOutProxyInfoUpdate(ProxyInfo proxyInfo) {
        super("ProxyInfoUpdate", new Configuration().addProperty("info", proxyInfo));
    }
}
