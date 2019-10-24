/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.proxy.settings.ProxySettings;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 06.04.2019
 */

public final class PacketOutGetProxyConfig extends DefaultPacket implements Serializable {

    public PacketOutGetProxyConfig(UUID result, Optional<ProxySettings> proxySettings) {
        super(
            "GetProxyConfig",
            new Configuration().addValue("settings", proxySettings),
            result
        );
    }
}
