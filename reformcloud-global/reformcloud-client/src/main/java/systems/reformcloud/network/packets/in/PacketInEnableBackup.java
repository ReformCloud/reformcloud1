/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.backup.BackupHelper;
import systems.reformcloud.backup.FTPConfig;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 12.05.2019
 */

public final class PacketInEnableBackup implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        FTPConfig ftpConfig = configuration.getValue("config", new TypeToken<FTPConfig>() {
        });
        if (ftpConfig != null && BackupHelper.getInstance() == null)
            new BackupHelper(ftpConfig);
    }
}
