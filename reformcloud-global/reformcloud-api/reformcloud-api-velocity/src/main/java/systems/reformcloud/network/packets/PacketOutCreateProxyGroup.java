/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 25.04.2019
 */

public final class PacketOutCreateProxyGroup extends DefaultPacket implements Serializable {

    public PacketOutCreateProxyGroup(ProxyGroup proxyGroup) {
        super("CreateProxyGroup", new Configuration().addValue("group", proxyGroup));
    }
}
