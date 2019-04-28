/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.examples.event;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.event.annotation.Handler;
import systems.reformcloud.event.utility.Listener;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 20.04.2019
 */

public final class ListenerExample implements Serializable, Listener {
    /**
     * Handles an event
     *
     * @param event The event which should be handled
     */
    @Handler
    public void handle(final EventExample event) {
        System.out.println(event.getEvent().getClass());
    }

    /**
     * Registers the Listener
     */
    public ListenerExample() {
        ReformCloudController.getInstance().getEventManager().registerListener(this);
    }

    /**
     * Calls the example event
     */
    public void callExampleEvent() {
        ReformCloudController.getInstance().getEventManager().fire(new EventExample(null));
    }
}
