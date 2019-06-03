/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.in;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.dev.DevProcess;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.query.out.PacketOutStartDevProcessResult;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 24.04.2019
 */

public final class PacketInQueryStartDevProcess implements Serializable,
    NetworkQueryInboundHandler {

    @Override
    public void handle(Configuration configuration, UUID resultID) {
        ServerGroup serverGroup = configuration.getValue("group", new TypeToken<ServerGroup>() {
        });
        String template =
            configuration.contains("template") ? configuration.getStringValue("template")
                : "default";
        Configuration preConfig =
            configuration.contains("pre") ? configuration.getConfiguration("pre")
                : new Configuration();

        DevProcess devProcess = new DevProcess(
            serverGroup,
            preConfig,
            template,
            System.currentTimeMillis()
        );
        ReformCloudController.getInstance().getCloudProcessOfferService().getDevProcesses()
            .offer(devProcess);
        ReformCloudController.getInstance().getChannelHandler().sendDirectPacket(
            configuration.getStringValue("from"),
            new PacketOutStartDevProcessResult(resultID, devProcess)
        );
    }
}
