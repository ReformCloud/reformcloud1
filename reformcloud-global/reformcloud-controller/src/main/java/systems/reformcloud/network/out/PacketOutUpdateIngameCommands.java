/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 19.03.2019
 */

public final class PacketOutUpdateIngameCommands extends DefaultPacket implements Serializable {

    public PacketOutUpdateIngameCommands() {
        super("UpdateIngameCommands", new Configuration().addValue("commands",
            ReformCloudController.getInstance().getIngameCommandManger()
                .getAllRegisteredCommands()));
    }
}
