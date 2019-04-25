/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 25.04.2019
 */

public final class PacketInUpdateProxyGroup implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        ProxyGroup proxyGroup = configuration.getValue("group", new TypeToken<ProxyGroup>() {
        });
        if (proxyGroup == null)
            return;

        ReformCloudController.getInstance().getCloudConfiguration().updateProxyGroup(proxyGroup);
    }
}
