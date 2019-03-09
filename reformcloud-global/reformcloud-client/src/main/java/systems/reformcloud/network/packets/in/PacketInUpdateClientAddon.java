/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.files.DownloadManager;
import systems.reformcloud.utility.files.FileUtils;

import java.io.Serializable;
import java.nio.file.Paths;

/**
 * @author _Klaro | Pasqual K. / created on 09.03.2019
 */

public final class PacketInUpdateClientAddon implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        FileUtils.deleteFileIfExists(Paths.get("addons/" + configuration.getStringValue("name") + ".jar"));
        DownloadManager.downloadAndDisconnect("clientAddon", configuration.getStringValue("url"),
                "addons/" + configuration.getStringValue("name") + ".jar");
    }
}
