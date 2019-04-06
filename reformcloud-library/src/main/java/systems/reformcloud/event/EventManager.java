/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event;

import systems.reformcloud.event.enums.EventTargetType;
import systems.reformcloud.event.events.*;
import systems.reformcloud.event.utility.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

public final class EventManager {
    private final List<Listener> listeners = new ArrayList<>();

    public void registerListener(final Listener listener) {
        listeners.add(listener);
    }

    public void unregisterListner(final Listener listener) {
        listeners.remove(listener);
    }

    public void unregisterAllListener() {
        listeners.clear();
    }

    public void callEvent(final EventTargetType eventTargetType, final Event event) {
        if (!event.isCallable())
            throw new IllegalStateException("Cannot call an event which isn't callable");

        listeners.stream()
                .filter(e -> e.getEventTargetType().equals(eventTargetType))
                .forEach(e -> handleEvent(event, eventTargetType.name(), e));
    }

    private void handleEvent(final Event event, final String name, final Listener listener) {
        if (name.equals(EventTargetType.INCOMING_PACKET.name()))
            listener.handle((IncomingPacketEvent) event);
        else if (name.equals(EventTargetType.LOAD_SUCCESS.name()))
            listener.handle((LoadSuccessEvent) event);
        else if (name.equals(EventTargetType.OUTGOING_PACKET.name()))
            listener.handle((OutGoingPacketEvent) event);
        else if (name.equals(EventTargetType.PACKET_HANDLE_SUCCESS.name()))
            listener.handle((PacketHandleSuccessEvent) event);
        else if (name.equals(EventTargetType.PROCESS_REGISTERED.name()))
            listener.handle((ProcessRegisterEvent) event);
        else if (name.equals(EventTargetType.PROCESS_UNREGISTERED.name()))
            listener.handle((ProcessUnregistersEvent) event);
        else
            throw new IllegalStateException("Cannot resole Event Type, or its undefined");
    }
}
