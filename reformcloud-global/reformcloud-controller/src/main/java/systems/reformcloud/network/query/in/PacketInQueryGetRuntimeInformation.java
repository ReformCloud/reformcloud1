/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.in;

import java.io.Serializable;
import java.util.UUID;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.system.RuntimeSnapshot;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.out.PacketOutGetRuntimeInformation;

/**
 * @author _Klaro | Pasqual K. / created on 28.06.2019
 */

public final class PacketInQueryGetRuntimeInformation implements Serializable,
    NetworkQueryInboundHandler {

    @Override
    public void handle(Configuration configuration, UUID resultID) {
        String of = configuration.getStringValue("of");
        String type = configuration.getStringValue("type");
        RuntimeSnapshot runtimeSnapshot;

        switch (type) {
            case "client":
                runtimeSnapshot =
                    ReformCloudController.getInstance().shiftClientRuntimeSnapshot(of);
                break;
            case "proxy":
                runtimeSnapshot =
                    ReformCloudController.getInstance().shiftProxyRuntimeSnapshot(of);
                break;
            case "server":
                runtimeSnapshot =
                    ReformCloudController.getInstance().shiftServerRuntimeSnapshot(of);
                break;
            default:
                runtimeSnapshot =
                    ReformCloudController.getInstance().shiftControllerRuntimeSnapshot();
        }

        if (runtimeSnapshot == null) {
            return;
        }

        ReformCloudController.getInstance().getChannelHandler()
            .sendPacketSynchronized(
                configuration.getStringValue("from"),
                new PacketOutGetRuntimeInformation(
                    runtimeSnapshot, resultID
                )
            );
    }
}
