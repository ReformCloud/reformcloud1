/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.in;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.files.ZoneInformationProtocolUtility;

import java.io.Serializable;
import java.util.Base64;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 15.03.2019
 */

public final class PacketInGetControllerTemplateResult implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        String type = configuration.getStringValue("type");
        switch (type.toLowerCase()) {
            case "server": {
                byte[] result = Base64.getDecoder().decode(configuration.getStringValue("encode"));
                ServerGroup serverGroup = ReformCloudClient.getInstance().getInternalCloudNetwork()
                        .getServerGroups().get(configuration.getStringValue("group"));
                if (serverGroup.getServerModeType().equals(ServerModeType.STATIC)) {
                    try {
                        ZoneInformationProtocolUtility.unZip(
                                result,
                                "reformcloud/static/servers/" + configuration.getStringValue("name")
                        );
                    } catch (final Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        ZoneInformationProtocolUtility.unZip(
                                result,
                                "reformcloud/temp/servers/" + configuration.getStringValue("name")
                                        + "-" + configuration.getValue("uuid", UUID.class)
                        );
                    } catch (final Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            }

            case "proxy": {
                byte[] result = Base64.getDecoder().decode(configuration.getStringValue("encode"));
                try {
                    ZoneInformationProtocolUtility.unZip(
                            result,
                            "reformcloud/temp/proxies/" + configuration.getStringValue("name")
                                    + "-" + configuration.getValue("uuid", UUID.class)
                    );
                } catch (final Exception ex) {
                    ex.printStackTrace();
                }
                break;
            }
        }
    }
}
