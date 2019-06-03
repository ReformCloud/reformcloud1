/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.files.FileUtils;

import java.io.File;
import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 03.05.2019
 */

public final class PacketInDeleteTemplate implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        switch (configuration.getStringValue("type").toLowerCase()) {
            case "proxy": {
                File file = new File("reformcloud/templates/proxies/" +
                    configuration.getStringValue("group") + "/" +
                    configuration.getStringValue("template"));
                FileUtils.deleteFullDirectory(file);
                break;
            }
            case "server": {
                File file = new File("reformcloud/templates/servers/" +
                    configuration.getStringValue("group") + "/" +
                    configuration.getStringValue("template"));
                FileUtils.deleteFullDirectory(file);
                break;
            }
        }
    }
}
