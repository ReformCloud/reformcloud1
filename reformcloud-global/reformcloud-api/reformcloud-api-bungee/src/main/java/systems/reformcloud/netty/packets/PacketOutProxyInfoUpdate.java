/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.netty.packet.enums.PacketSender;
import systems.reformcloud.netty.packet.enums.QueryType;

import java.util.Collections;

/**
 * @author _Klaro | Pasqual K. / created on 06.12.2018
 */

public final class PacketOutProxyInfoUpdate extends Packet {
    public PacketOutProxyInfoUpdate(final ProxyInfo proxyInfo) {
        super("ProxyInfoUpdate", new Configuration().addProperty("proxyInfo", proxyInfo), Collections.singletonList(QueryType.COMPLETE), PacketSender.PROCESS_PROXY);
    }
}
