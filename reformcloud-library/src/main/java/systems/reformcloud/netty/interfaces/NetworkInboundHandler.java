/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.interfaces;

import systems.reformcloud.configurations.Configuration;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

public interface NetworkInboundHandler {
    void handle(Configuration configuration);
}
