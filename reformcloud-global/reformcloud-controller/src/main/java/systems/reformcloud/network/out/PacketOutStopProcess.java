/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public final class PacketOutStopProcess extends Packet {

    public PacketOutStopProcess(final String name) {
        super("StopProcess", new Configuration().addStringValue("name", name));
    }
}
