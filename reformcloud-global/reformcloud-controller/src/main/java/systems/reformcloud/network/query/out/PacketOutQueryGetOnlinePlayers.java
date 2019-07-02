/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.out;

import java.io.Serializable;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

/**
 * @author _Klaro | Pasqual K. / created on 02.07.2019
 */

public final class PacketOutQueryGetOnlinePlayers extends Packet implements Serializable {

    public PacketOutQueryGetOnlinePlayers() {
        super("GetOnlinePlayers", new Configuration());
    }

}
