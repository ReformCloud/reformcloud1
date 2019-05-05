/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 24.04.2019
 */

public final class PacketInUpdateServerGroup implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        ServerGroup serverGroup = configuration.getValue("group", new TypeToken<ServerGroup>() {
        });
        if (serverGroup == null)
            return;

        ReformCloudController.getInstance().getCloudConfiguration().updateServerGroup(serverGroup);
    }
}
