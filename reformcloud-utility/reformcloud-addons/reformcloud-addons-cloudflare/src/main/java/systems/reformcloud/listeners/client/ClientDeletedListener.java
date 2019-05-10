/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.listeners.client;

import systems.reformcloud.CloudFlareUtil;
import systems.reformcloud.event.annotation.Handler;
import systems.reformcloud.event.utility.Listener;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.05.2019
 */

public final class ClientDeletedListener implements Serializable, Listener {
    @Handler
    public void handleDelete(final systems.reformcloud.api.events.ClientDeletedEvent event) {
        CloudFlareUtil.getInstance().deleteClientEntry(event.getClient());
    }
}
