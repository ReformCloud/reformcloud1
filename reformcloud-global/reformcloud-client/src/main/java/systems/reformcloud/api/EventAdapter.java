/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 23.02.2019
 */

public final class EventAdapter implements Serializable, IEventHandler {
    public EventAdapter() {
        this.instance.set(this);
    }

    @Override
    public void handleCustomPacket(String channel, String targetType, Configuration configuration) {
    }

    @Override
    public void handleReload() {
        ReformCloudClient.getInstance().reloadAll();
    }
}
