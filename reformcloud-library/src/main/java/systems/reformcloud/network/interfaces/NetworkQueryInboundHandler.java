/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.interfaces;

import systems.reformcloud.configurations.Configuration;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 22.02.2019
 */

public interface NetworkQueryInboundHandler {
    /**
     * Handler for an incoming query packet
     *
     * @param configuration     The configuration of the query packet
     * @param resultID          The result uid of the query packet
     */
    void handle(Configuration configuration, UUID resultID);
}
