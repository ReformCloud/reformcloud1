/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.sync.out;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 11.02.2019
 */

public final class PacketOutSyncExceptionThrown extends Packet implements Serializable {
    public PacketOutSyncExceptionThrown(final Throwable cause) {
        super("ExceptionThrown", new Configuration()
                .addStringProperty("name", ReformCloudClient.getInstance().getCloudConfiguration().getClientName())
                .addProperty("cause", cause));
    }
}
