/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.event.annotation.Handler;
import systems.reformcloud.event.utility.Event;
import systems.reformcloud.logging.AbstractLoggerProvider;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

public final class EventManager implements Serializable {
    /**
     * The map contains all registered listeners and their handlers
     */
    protected Map<Class<?>, EventClass[]> handlers = ReformCloudLibraryService.concurrentHashMap();

    /**
     * Consumer handel all fired events
     */
    private Consumer<Object> fireAndForget = this::callEvent;

    /**
     * Get the handling events of the class
     *
     * @param listener      The listener class which should be created
     * @return A map containing the handling events and the handlers in the event class itself
     */
    private Map<Class<?>, Set<Method>> getHandling(Object listener) {
        Map<Class<?>, Set<Method>> response = new HashMap<>();
        for (Method method : listener.getClass().getDeclaredMethods()) {
            Handler handler = method.getAnnotation(Handler.class);
            if (!Modifier.isPublic(method.getModifiers()) || !method.getReturnType().equals(Void.TYPE))
                continue;

            if (handler != null) {
                Class<?>[] parameters = method.getParameterTypes();
                if (parameters.length != 1) {
                    AbstractLoggerProvider.defaultLogger().serve().accept("An handler tried to register a listener with"
                            + parameters.length + "parameter(s)");
                    continue;
                }

                Set<Method> handling = response.get(parameters[0]);
                if (handling == null)
                    handling = new HashSet<>();

                handling.add(method);
                response.put(parameters[0], handling);
            }
        }

        return response;
    }

    /**
     * Registers a specific listener
     *
     * @param listener      The listener which should be registered
     */
    private void registerListener0(Object listener) {
        Map<Class<?>, Set<Method>> handling = this.getHandling(listener);
        List<EventClass> done = new ArrayList<>();
        for (Map.Entry<Class<?>, Set<Method>> methods : handling.entrySet()) {
            for (Method method : methods.getValue()) {
                EventClass eventClass = new EventClass(listener, method);
                done.add(eventClass);
            }

            EventClass[] eventClasses = new EventClass[done.size()];
            this.handlers.put(methods.getKey(), done.toArray(eventClasses));
        }
    }

    /**
     * Calls an event
     *
     * @param event     The event which should be called
     */
    private void callEvent(Object event) {
        EventClass[] eventClasses = this.handlers.get(event.getClass());
        if (eventClasses != null) {
            for (EventClass eventClass : eventClasses) {
                try {
                    eventClass.invoke(event);
                } catch (final InvocationTargetException | IllegalAccessException ex) {
                    AbstractLoggerProvider.defaultLogger().exception().accept(ex);
                }
            }
        }
    }

    /**
     * Calls an event
     *
     * @param event     The event which should be
     * @param <T>       The event which should be called, extending the event class
     */
    public <T extends Event> void fire(T event) {
        this.fireAndForget.accept(event);
    }

    /**
     * Registers a listener
     *
     * @param listener      The listener which should be registered
     */
    public void registerListener(Object listener) {
        this.registerListener0(listener);
    }

    /**
     * Unregisters all registered listeners
     */
    public void unregisterAll() {
        this.handlers.clear();
    }
}
