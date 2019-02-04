/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event;

import systems.reformcloud.event.enums.EventTargetType;
import lombok.Getter;
import systems.reformcloud.event.events.*;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

@Getter
public abstract class Listener {
    public Listener(String name, EventTargetType eventTargetType) {
        this.name = name;
        this.eventTargetType = eventTargetType;
    }

    private String name;
    private EventTargetType eventTargetType;

    public void handle(LoadSuccessEvent event) {
    }

    public void handle(PacketHandleSuccessEvent event) {
    }

    public void handle(IncomingPacketEvent event) {
    }

    public void handle(OutGoingPacketEvent event) {
    }

    public void handle(ProcessRegisterEvent event) {
    }

    public void handle(ProcessUnregistersEvent event) {
    }
}
