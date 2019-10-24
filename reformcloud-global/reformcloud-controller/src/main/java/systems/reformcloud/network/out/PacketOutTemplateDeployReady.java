/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 11.04.2019
 */

public final class PacketOutTemplateDeployReady extends DefaultPacket implements Serializable {

    public PacketOutTemplateDeployReady(String group, String template, boolean proxy) {
        super("TemplateDeployReady", new Configuration()
            .addStringValue("group", group)
            .addStringValue("template", template)
            .addBooleanValue("proxy", proxy)
        );
    }
}
