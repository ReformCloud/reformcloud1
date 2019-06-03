/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api.events;

import systems.reformcloud.event.utility.Event;
import systems.reformcloud.meta.client.Client;

import java.beans.ConstructorProperties;
import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.05.2019
 */

public final class ClientDeletedEvent extends Event implements Serializable {

    private Client client;

    @ConstructorProperties({"client"})
    public ClientDeletedEvent(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
}
