/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import java.io.Serializable;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

/**
 * @author _Klaro | Pasqual K. / created on 12.05.2019
 */

public final class PacketOutDisableBackup extends Packet implements Serializable {
    public PacketOutDisableBackup() {
        super("DisableBackup", new Configuration());
    }
}
