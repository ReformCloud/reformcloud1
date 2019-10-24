/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 12.03.2019
 */

public final class PacketInProxyInfoUpdate implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        ProxyInfo proxyInfo = configuration.getValue("info", TypeTokenAdaptor.getPROXY_INFO_TYPE());
        ReformCloudAPIVelocity.getInstance().updateProxyInfoInternal(proxyInfo);
    }
}
