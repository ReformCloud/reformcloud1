/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 06.04.2019
 */

public final class PacketOutGetProxySettings extends Packet implements Serializable {
    public PacketOutGetProxySettings() {
        super("GetProxyConfig", new Configuration());
    }
}
