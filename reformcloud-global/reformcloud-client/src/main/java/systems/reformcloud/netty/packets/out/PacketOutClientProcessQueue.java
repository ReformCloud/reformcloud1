/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.packets.out;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.startup.ProxyStartupInfo;
import systems.reformcloud.meta.startup.ServerStartupInfo;
import systems.reformcloud.netty.packet.Packet;

import java.io.Serializable;
import java.util.Queue;

/**
 * @author _Klaro | Pasqual K. / created on 07.02.2019
 */

public final class PacketOutClientProcessQueue extends Packet implements Serializable {
    private static final long serialVersionUID = -4401703259647752493L;

    public PacketOutClientProcessQueue(final Queue<ServerStartupInfo> servers, final Queue<ProxyStartupInfo> proxies) {
        super(
                "ClientProcessQueue",
                new Configuration()
                        .addStringProperty("name", ReformCloudClient.getInstance().getCloudConfiguration().getClientName())
                        .addProperty("servers", servers)
                        .addProperty("proxies", proxies)
        );
    }
}
