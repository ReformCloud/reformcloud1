/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packet.enums.PacketSender;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.util.Collections;

/**
 * @author _Klaro | Pasqual K. / created on 26.12.2018
 */

public final class PacketOutStartProxy extends Packet {
    public PacketOutStartProxy(final ProxyGroup proxyGroup, final Configuration preConfig) {
        super("StartProxyProcess", new Configuration().addProperty("group", proxyGroup).addConfigurationProperty("pre", preConfig), Collections.singletonList(QueryType.COMPLETE), PacketSender.PROCESS_SERVER);
    }

    public PacketOutStartProxy(final ProxyGroup proxyGroup, final Configuration preConfig, final String template) {
        super("StartProxyProcess", new Configuration().addProperty("group", proxyGroup).addStringProperty("template", template).addConfigurationProperty("pre", preConfig), Collections.singletonList(QueryType.COMPLETE), PacketSender.PROCESS_PROXY);
    }
}
