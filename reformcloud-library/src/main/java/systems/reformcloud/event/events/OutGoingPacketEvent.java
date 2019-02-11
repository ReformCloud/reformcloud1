/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.reformcloud.event.utility.Event;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

@Getter
@AllArgsConstructor
public class OutGoingPacketEvent extends Event implements Serializable {
    private static final long serialVersionUID = -124171615796476015L;

    private boolean cancelled;
    private Packet packet;

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }
}
