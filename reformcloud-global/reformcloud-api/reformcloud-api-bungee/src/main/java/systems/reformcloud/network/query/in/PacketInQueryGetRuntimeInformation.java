/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.in;

import java.io.Serializable;
import java.util.UUID;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.system.RuntimeSnapshot;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.packets.PacketOutGetRuntimeInformation;

/**
 * @author _Klaro | Pasqual K. / created on 28.06.2019
 */

public final class PacketInQueryGetRuntimeInformation implements Serializable,
    NetworkQueryInboundHandler {

    @Override
    public void handle(Configuration configuration, UUID resultID) {
        RuntimeSnapshot runtimeSnapshot =
            ReformCloudAPIBungee.getInstance().current();

        ReformCloudAPIBungee.getInstance().getChannelHandler()
            .sendPacketSynchronized(
                configuration.getStringValue("from"),
                new PacketOutGetRuntimeInformation(
                    runtimeSnapshot, resultID
                )
            );
    }
}
