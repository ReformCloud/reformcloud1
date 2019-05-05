/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.out.PacketOutGetControllerTemplateResult;
import systems.reformcloud.utility.files.FileUtils;
import systems.reformcloud.utility.files.ZoneInformationProtocolUtility;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 15.03.2019
 */

public final class PacketInGetControllerTemplate implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        String groupName = configuration.getStringValue("groupName");
        String template = configuration.getStringValue("template");
        String type = configuration.getStringValue("type");
        String client = configuration.getStringValue("requester");

        switch (type.toLowerCase()) {
            case "proxy": {
                if (!Files.exists(Paths.get("reformcloud/templates/proxies/" + groupName + "/" + template))) {
                    FileUtils.createDirectory(Paths.get("reformcloud/templates/proxies/" + groupName + "/" + template));
                }

                byte[] zippedTemplate = ZoneInformationProtocolUtility.zipDirectoryToBytes(
                        Paths.get("reformcloud/templates/proxies/" + groupName + "/" + template)
                );

                ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                        client, new PacketOutGetControllerTemplateResult(
                                Base64.getEncoder().encodeToString(zippedTemplate),
                                configuration.getStringValue("name"),
                                configuration.getValue("uuid", UUID.class),
                                type,
                                groupName
                        )
                );
                break;
            }

            case "server": {
                if (!Files.exists(Paths.get("reformcloud/templates/servers/" + groupName + "/" + template))) {
                    FileUtils.createDirectory(Paths.get("reformcloud/templates/servers/" + groupName + "/" + template));
                }

                byte[] zippedTemplate = ZoneInformationProtocolUtility.zipDirectoryToBytes(
                        Paths.get("reformcloud/templates/servers/" + groupName + "/" + template)
                );

                ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                        client, new PacketOutGetControllerTemplateResult(
                                Base64.getEncoder().encodeToString(zippedTemplate),
                                configuration.getStringValue("name"),
                                configuration.getValue("uuid", UUID.class),
                                type,
                                groupName
                        )
                );
                break;
            }
        }
    }
}
