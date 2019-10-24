/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.ParametersAddon;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 14.04.2019
 */

public final class PacketOutParametersUpdate extends DefaultPacket implements Serializable {

    public PacketOutParametersUpdate() {
        super(
            "ParametersUpdate",
            new Configuration().addValue("parameters",
                ParametersAddon.getInstance().getParametersConfiguration().getParameters())
        );
    }
}
