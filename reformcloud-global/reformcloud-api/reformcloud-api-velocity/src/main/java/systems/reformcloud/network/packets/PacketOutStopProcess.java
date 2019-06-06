/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 25.04.2019
 */

public final class PacketOutStopProcess extends Packet implements Serializable {

    public PacketOutStopProcess(String id) {
        super("StopProcess", new Configuration().addStringValue("name", id));
    }
}
