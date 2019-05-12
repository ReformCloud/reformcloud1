/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.query.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 12.05.2019
 */

public final class PacketQueryOutGetFTPConfig extends Packet implements Serializable {
    public PacketQueryOutGetFTPConfig() {
        super("GetFTPConfig", new Configuration());
    }
}
