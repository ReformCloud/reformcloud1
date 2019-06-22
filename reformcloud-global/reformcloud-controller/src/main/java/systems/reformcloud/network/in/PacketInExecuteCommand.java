/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 22.06.2019
 */

public final class PacketInExecuteCommand implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        String type = configuration.getStringValue("type");
        switch (type) {
            case "server": {
                ReformCloudController.getInstance().executeCommandOnServer(
                    configuration.getStringValue("name"),
                    configuration.getStringValue("command")
                );
                break;
            }
            case "proxy": {
                ReformCloudController.getInstance().executeCommandOnProxy(
                    configuration.getStringValue("name"),
                    configuration.getStringValue("command")
                );
                break;
            }
        }
    }
}
