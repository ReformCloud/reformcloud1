/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.utility;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

public interface Cancellable {
    /**
     * Set if the event is cancelled or not
     *
     * @param cancelled     The new cancel status
     */
    void setCancelled(boolean cancelled);

    /**
     * Gets the current cancel status
     *
     * @return      If the current event is cancelled
     */
    default boolean isCancelled() {
        return false;
    }
}
