/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.examples.event;

import lombok.Getter;
import systems.reformcloud.event.utility.Cancellable;
import systems.reformcloud.event.utility.Event;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

public final class EventExample extends Event implements Serializable, Cancellable {
    /**
     * The current cancel status
     */
    private boolean cancelled;

    /**
     * Sets the cancel status
     *
     * @param cancelled     The new cancel status
     */
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Get if the event is currently cancelled
     *
     * @return If the event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Creates the new event
     *
     * @param event     The event which is needed for that event (If you call this event why did you created it?
     *                  - Our event should handle if an event is called so we have the called event in the constructor)
     */
    public EventExample(Event event) {
        this.event = event;
    }

    /**
     * Get the event content
     */
    @Getter
    private Event event;
}
