/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 02.07.2019
 */

public final class PacketOutQueryGetOnlinePlayers extends DefaultPacket implements Serializable {

    public PacketOutQueryGetOnlinePlayers() {
        super("GetOnlinePlayers", new Configuration());
    }

}
