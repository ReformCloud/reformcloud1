/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import systems.reformcloud.backup.BackupHelper;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 12.05.2019
 */

public final class PacketInDisableBackup implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        if (BackupHelper.getInstance() != null)
            BackupHelper.getInstance().close();
    }
}
