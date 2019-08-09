/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.backup.FTPConfig;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 12.05.2019
 */

public final class PacketOutEnableBackup extends DefaultPacket implements Serializable {

    public PacketOutEnableBackup(FTPConfig ftpConfig) {
        super("EnableBackup", new Configuration().addValue("config", ftpConfig));
    }
}
