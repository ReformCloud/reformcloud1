/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.listeners;

import systems.reformcloud.CloudFlareUtil;
import systems.reformcloud.api.events.ClientCreatedEvent;
import systems.reformcloud.api.events.ClientDeletedEvent;
import systems.reformcloud.event.annotation.Handler;
import systems.reformcloud.event.utility.Listener;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.05.2019
 */

public final class ClientListener implements Serializable, Listener {
    @Handler
    public void handle(final ClientCreatedEvent event) {
        CloudFlareUtil.getInstance().createClientEntry(event.getClient());
    }

    @Handler
    public void handle(final ClientDeletedEvent event) {
        CloudFlareUtil.getInstance().deleteClientEntry(event.getClient());
    }
}
