/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.authentication.enums.AuthenticationType;
import systems.reformcloud.network.packet.Packet;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public final class PacketOutInternalProcessRemove extends Packet {
    public PacketOutInternalProcessRemove(final UUID processUID, final AuthenticationType authenticationType) {
        super("InternalProcessRemove", new Configuration().addValue("uid", processUID).addStringValue("type", authenticationType.name()));
    }
}
