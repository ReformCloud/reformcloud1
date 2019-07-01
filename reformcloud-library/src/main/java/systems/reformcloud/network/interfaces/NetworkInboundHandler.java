/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.interfaces;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.constants.ChannelConstants;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

public interface NetworkInboundHandler {

    /**
     * Handles an incoming packet
     *
     * @param configuration The configuration of the packet
     */
    void handle(Configuration configuration);

    default long handlingChannel() {
        return ChannelConstants.REFORMCLOUD_INTERNAL_INFORMATION_DEFAULT_CHANNEL;
    }
}
