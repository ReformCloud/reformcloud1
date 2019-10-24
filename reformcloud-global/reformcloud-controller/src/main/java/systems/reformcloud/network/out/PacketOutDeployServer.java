/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.04.2019
 */

public final class PacketOutDeployServer extends DefaultPacket implements Serializable {

    public PacketOutDeployServer(String name, String template, boolean proxy, String toClient) {
        super("DeployServer", new Configuration()
            .addStringValue("group", name)
            .addStringValue("template", template)
            .addStringValue("type", (proxy ? "proxy" : "server"))
            .addStringValue("to", toClient)
        );
    }
}
