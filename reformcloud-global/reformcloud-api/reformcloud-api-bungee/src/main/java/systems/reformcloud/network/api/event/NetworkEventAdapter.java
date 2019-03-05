/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.api.event;

import systems.reformcloud.api.IEventHandler;
import systems.reformcloud.configurations.Configuration;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 05.03.2019
 */

public final class NetworkEventAdapter implements Serializable, IEventHandler {
    @Override
    public void handleCustomPacket(String channel, String targetType, Configuration configuration) {
    }

    @Override
    public void handleReload() {
    }
}
