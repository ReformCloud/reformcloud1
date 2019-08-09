/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.system.RuntimeSnapshot;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 28.06.2019
 */

public final class PacketOutGetRuntimeInformation extends DefaultPacket implements Serializable {

    public PacketOutGetRuntimeInformation(RuntimeSnapshot answer, UUID result) {
        super("GetRuntimeInformation", new Configuration().addValue("info",
            answer), result);
    }
}
