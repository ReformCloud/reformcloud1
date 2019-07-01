/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.interfaces;

import java.util.UUID;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.constants.ChannelConstants;

/**
 * @author _Klaro | Pasqual K. / created on 22.02.2019
 */

public interface NetworkQueryInboundHandler {

    /**
     * Handler for an incoming query packet
     *
     * @param configuration The configuration of the query packet
     * @param resultID The result uid of the query packet
     */
    void handle(Configuration configuration, UUID resultID);

    default long handlingChannel() {
        return ChannelConstants.REFORMCLOUD_INTERNAL_QUERY_INFORMATION_DEFAULT_CHANNEL;
    }
}
