/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.deploy.incoming.ControllerTemplateDownload;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 11.04.2019
 */

public final class PacketInTemplateDeployReady implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        new ControllerTemplateDownload().download(
                configuration.getStringValue("group"),
                configuration.getStringValue("template"),
                configuration.getBooleanValue("proxy")
        );
    }
}
