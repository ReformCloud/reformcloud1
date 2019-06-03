/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 25.04.2019
 */

public final class PacketOutCreateServerGroup extends Packet implements Serializable {

    public PacketOutCreateServerGroup(ServerGroup serverGroup) {
        super("CreateServerGroup", new Configuration().addValue("group", serverGroup));
    }
}
