/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.out;

import java.io.Serializable;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

/**
 * @author _Klaro | Pasqual K. / created on 28.06.2019
 */

public final class PacketOutQueryGetRuntimeInformation extends Packet implements Serializable {

    public PacketOutQueryGetRuntimeInformation(String serverName, String type) {
        super(
            "GetRuntimeInformation",
            new Configuration().addStringValue("of", serverName).addStringValue("type", type)
        );
    }

}
