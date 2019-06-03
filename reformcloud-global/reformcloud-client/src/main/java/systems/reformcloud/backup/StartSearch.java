/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.backup;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.network.packets.query.out.PacketQueryOutGetFTPConfig;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 12.05.2019
 */

public final class StartSearch implements Serializable {

    public StartSearch() {
        ReformCloudClient.getInstance().getChannelHandler().sendPacketQuerySync(
            "ReformCloudController",
            "ReformCloudClient",
            new PacketQueryOutGetFTPConfig(),
            (configuration, resultID) -> {
                FTPConfig ftpConfig = configuration.getValue("config", new TypeToken<FTPConfig>() {
                });
                if (ftpConfig != null) {
                    new BackupHelper(ftpConfig);
                }
            }
        );
    }
}
