/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.abstracts;

import systems.reformcloud.event.utility.Event;
import systems.reformcloud.utility.annotiations.ShouldNotBeNull;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 20.06.2019
 */

public abstract class EventManager implements Serializable {

    /**
     * Calls an event
     *
     * @param arg1 The event which should be
     * @param <T>  The event which should be called, extending the event class
     */
    public abstract <T extends Event> void fire(@ShouldNotBeNull T arg1);

    /**
     * Registers a listener
     *
     * @param arg1 The listener which should be registered
     */
    public abstract void registerListener(@ShouldNotBeNull Object arg1);

    /**
     * Unregisters all registered listeners
     */
    public abstract void unregisterAll();

    /**
     * Unregisters all listeners by the given class loader
     *
     * @param arg1 The class loader of the class which registered the listeners
     */
    public abstract void unregisterListenerByClassLoader(ClassLoader arg1);
}
