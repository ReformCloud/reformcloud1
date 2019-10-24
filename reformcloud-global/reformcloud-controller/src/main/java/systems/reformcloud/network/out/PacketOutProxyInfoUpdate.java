/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 12.03.2019
 */

public final class PacketOutProxyInfoUpdate extends DefaultPacket implements Serializable {

    public PacketOutProxyInfoUpdate(ProxyInfo proxyInfo) {
        super("ProxyInfoUpdate", new Configuration().addValue("info", proxyInfo));
    }
}
