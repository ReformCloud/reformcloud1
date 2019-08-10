/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.event;

import systems.reformcloud.event.abstracts.EventManager;
import systems.reformcloud.event.utility.Event;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public abstract class EventEvent extends Event implements Serializable {

    public EventEvent(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    private final EventManager eventManager;

    public EventManager getEventManager() {
        return eventManager;
    }
}
