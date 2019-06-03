/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.proxy.settings.ProxySettings;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author _Klaro | Pasqual K. / created on 06.04.2019
 */

public final class PacketInUpdateProxySettings implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        Optional<ProxySettings> proxySettings = configuration
            .getValue("settings", new TypeToken<Optional<ProxySettings>>() {
            }.getType());
        ReformCloudAPIBungee.getInstance().setProxySettings(proxySettings.orElse(null));
    }
}
