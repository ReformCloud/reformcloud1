/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.files.ZoneInformationProtocolUtility;

import java.io.Serializable;
import java.util.Base64;

/**
 * @author _Klaro | Pasqual K. / created on 15.03.2019
 */

public final class PacketInUpdateControllerTemplate implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        String type = configuration.getStringValue("type");
        switch (type.toLowerCase()) {
            case "server": {
                try {
                    ZoneInformationProtocolUtility.unZip(
                        Base64.getDecoder().decode(configuration.getStringValue("encoded")),
                        "reformcloud/templates/servers/" +
                            configuration.getStringValue("group") + "/" +
                            configuration.getStringValue("template")
                    );
                } catch (final Exception ex) {
                    ex.printStackTrace();
                }
                break;
            }
            case "proxy": {
                try {
                    ZoneInformationProtocolUtility.unZip(
                        Base64.getDecoder().decode(configuration.getStringValue("encoded")),
                        "reformcloud/templates/proxies/" +
                            configuration.getStringValue("group") + "/" +
                            configuration.getStringValue("template")
                    );
                } catch (final Exception ex) {
                    ex.printStackTrace();
                }
                break;
            }
        }
    }
}
