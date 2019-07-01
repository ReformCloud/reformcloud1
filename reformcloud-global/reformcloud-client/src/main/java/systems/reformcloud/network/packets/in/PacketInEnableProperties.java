/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.packets.out.PacketOutRequestProperties;
import systems.reformcloud.properties.DefaultPropertiesManager;
import systems.reformcloud.properties.PropertiesConfig;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 22.04.2019
 */

public final class PacketInEnableProperties implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        ReformCloudClient.getInstance().getChannelHandler().sendPacketQuerySync(
            "ReformCloudController",
            ReformCloudClient.getInstance().getCloudConfiguration().getClientName(),
            new PacketOutRequestProperties(),
            (configuration1, resultID) ->
                new DefaultPropertiesManager(
                    configuration1.getValue("config", new TypeToken<PropertiesConfig>() {
                    })),
            (configuration2, resultId) -> {
            }
        );
    }
}
