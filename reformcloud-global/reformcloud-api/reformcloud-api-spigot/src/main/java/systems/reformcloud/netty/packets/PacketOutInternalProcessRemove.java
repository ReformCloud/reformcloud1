/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.authentication.enums.AuthenticationType;
import systems.reformcloud.netty.packet.Packet;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public final class PacketOutInternalProcessRemove extends Packet {
    public PacketOutInternalProcessRemove(final UUID processUID, final AuthenticationType authenticationType) {
        super("InternalProcessRemove", new Configuration().addProperty("uid", processUID).addStringProperty("type", authenticationType.name()));
    }
}
