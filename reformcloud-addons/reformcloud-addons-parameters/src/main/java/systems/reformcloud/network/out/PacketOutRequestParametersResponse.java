/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import java.io.Serializable;
import java.util.UUID;
import systems.reformcloud.ParametersAddon;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

/**
 * @author _Klaro | Pasqual K. / created on 14.04.2019
 */

public final class PacketOutRequestParametersResponse extends Packet implements Serializable {
    public PacketOutRequestParametersResponse(UUID key) {
        super(
                "undefined",
                new Configuration().addValue("parameters", ParametersAddon.getInstance().getParametersConfiguration().getParameters()),
                key
        );
    }
}
