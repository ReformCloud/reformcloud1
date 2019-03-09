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

public final class PacketInUpdateSeverGroupPlugin implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        FileUtils.deleteFileIfExists(
                Paths.get("reformcloud/templates/" + configuration.getStringValue("groupName") + "/default/plugins/" +
                        configuration.getStringValue("pluginName") + ".jar")
        );
        FileUtils.createDirectory(Paths.get("reformcloud/templates/" + configuration.getStringValue("groupName") + "/default/plugins"));
        DownloadManager.downloadSilentAndDisconnect(configuration.getStringValue("url"),
                "reformcloud/templates/" + configuration.getStringValue("groupName") + "/default/plugins/" +
                        configuration.getStringValue("pluginName") + ".jar");
    }
}
