/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 12.05.2019
 */

public final class PacketOutDisableBackup extends DefaultPacket implements Serializable {

    public PacketOutDisableBackup() {
        super("DisableBackup", new Configuration());
    }
}
