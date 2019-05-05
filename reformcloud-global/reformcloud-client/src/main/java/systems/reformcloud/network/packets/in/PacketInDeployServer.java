/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.deploy.outgoing.ControllerTemplateDeploy;

import java.io.File;
import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.04.2019
 */

public final class PacketInDeployServer implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        boolean proxy = configuration.getStringValue("type").equals("proxy");
        new ControllerTemplateDeploy().deploy(
                new File(
                        "reformcloud/templates/" + (proxy ? "proxies" : "servers") + "/" +
                                configuration.getStringValue("group") + "/" + configuration.getStringValue("template")
                ),
                configuration.getStringValue("group"),
                configuration.getStringValue("template"),
                configuration.getStringValue("to"),
                proxy
        );
    }
}
