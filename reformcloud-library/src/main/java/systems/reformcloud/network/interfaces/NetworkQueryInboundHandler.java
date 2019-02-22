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
    void handle(Configuration configuration, UUID resultID);
}
