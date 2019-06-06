/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 25.04.2019
 */

public final class PacketOutCreateClient extends Packet implements Serializable {

    public PacketOutCreateClient(Client client) {
        super("CreateClient", new Configuration().addValue("client", client));
    }
}
