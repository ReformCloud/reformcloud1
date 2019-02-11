/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets.in;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.files.FileUtils;

import java.nio.file.Paths;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public class PacketInCopyServerIntoTemplate implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        switch (configuration.getStringValue("type").toLowerCase()) {
            case "server": {
                FileUtils.copyAllFiles(Paths.get("reformcloud/temp/servers/" + configuration.getStringValue("name")), "reformcloud/templates/" + configuration.getStringValue("group"), "spigot.jar");
                break;
            }
            case "proxy": {
                FileUtils.copyAllFiles(Paths.get("reformcloud/temp/proxies/" + configuration.getStringValue("name")), "reformcloud/templates/" + configuration.getStringValue("group"), "BungeeCord.jar");
                break;
            }
            default:
                break;
        }
    }
}
