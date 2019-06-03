/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.startup.ProxyStartupInfo;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

public final class PacketInStartProxy implements NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        final ProxyGroup proxyGroup = configuration
            .getValue("group", TypeTokenAdaptor.getPROXY_GROUP_TYPE());

        ReformCloudClient.getInstance().getCloudProcessStartupHandler()
            .offerProxyProcess(new ProxyStartupInfo(
                configuration.getValue("proxyProcess", UUID.class),
                configuration.getStringValue("name"),
                (configuration.contains("template") ? configuration.getStringValue("template")
                    : null), proxyGroup, configuration.getConfiguration("preConfig"),
                configuration.getIntegerValue("id")
            ));
    }
}
