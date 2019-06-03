/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.parameters.ParameterGroup;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 14.04.2019
 */

public final class PacketInParameterUpdate implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        ReformCloudClient.getInstance().getParameterManager()
            .update(configuration.getValue("parameters", new TypeToken<List<ParameterGroup>>() {
            }.getType()));
    }
}
