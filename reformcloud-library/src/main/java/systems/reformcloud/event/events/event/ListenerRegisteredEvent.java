/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.event;

import systems.reformcloud.event.abstracts.EventManager;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public final class ListenerRegisteredEvent extends EventEvent implements Serializable {

    public ListenerRegisteredEvent(EventManager eventManager, Object listener) {
        super(eventManager);
        this.listener = listener;
    }

    private final Object listener;

    public Object getListener() {
        return listener;
    }
}
