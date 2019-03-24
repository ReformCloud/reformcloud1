/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.network.packet.Packet;

/**
 * @author _Klaro | Pasqual K. / created on 26.12.2018
 */

public final class PacketOutStartProxy extends Packet {
    public PacketOutStartProxy(final ProxyGroup proxyGroup, final Configuration preConfig) {
        super("StartProxyProcess", new Configuration().addProperty("group", proxyGroup).addConfigurationProperty("pre", preConfig));
    }

    public PacketOutStartProxy(final ProxyGroup proxyGroup, final Configuration preConfig, final String template) {
        super("StartProxyProcess", new Configuration().addProperty("group", proxyGroup).addStringProperty("template", template).addConfigurationProperty("pre", preConfig));
    }
}
