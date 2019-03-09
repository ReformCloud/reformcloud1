/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.files.DownloadManager;
import systems.reformcloud.utility.files.FileUtils;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 09.03.2019
 */

public final class PacketInUpdateClientFromURL implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        DownloadManager.downloadAndDisconnect("the new client file", configuration.getStringValue("url"),
                FileUtils.getInternalFileName());
    }
}
