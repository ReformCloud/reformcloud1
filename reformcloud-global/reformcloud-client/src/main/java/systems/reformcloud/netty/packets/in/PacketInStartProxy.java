/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.startup.ProxyStartupInfo;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.util.List;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

public class PacketInStartProxy implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        final ProxyGroup proxyGroup = configuration.getValue("group", TypeTokenAdaptor.getProxyGroupType());

        ReformCloudClient.getInstance().getCloudProcessStartupHandler().offerProxyProcess(new ProxyStartupInfo(
                configuration.getValue("proxyProcess", UUID.class), configuration.getStringValue("name"), (configuration.contains("template") ? configuration.getStringValue("template") : null), proxyGroup, configuration.getConfiguration("preConfig"), configuration.getIntegerValue("id")
        ));
    }
}
