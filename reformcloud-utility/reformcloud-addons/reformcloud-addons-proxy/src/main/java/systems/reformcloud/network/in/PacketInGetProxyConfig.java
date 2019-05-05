/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ProxyAddon;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.out.PacketOutGetProxyConfig;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 06.04.2019
 */

public final class PacketInGetProxyConfig implements Serializable, NetworkQueryInboundHandler {
    @Override
    public void handle(Configuration configuration, UUID resultID) {
        ReformCloudController.getInstance().getChannelHandler().sendDirectPacket(
                configuration.getStringValue("from"),
                new PacketOutGetProxyConfig(resultID, ProxyAddon.getInstance().getProxyAddonConfiguration().getForProxy(
                        ReformCloudController.getInstance().getProxyInfo(configuration.getStringValue("from")).getCloudProcess().getGroup()
                ))
        );
    }
}
