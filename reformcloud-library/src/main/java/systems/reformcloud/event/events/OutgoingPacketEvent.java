/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events;

import lombok.Getter;
import systems.reformcloud.event.utility.Cancellable;
import systems.reformcloud.event.utility.Event;
import systems.reformcloud.network.packet.AwaitingPacket;

import java.io.Serializable;

/**
 * The event will be called when the cloud sends a packet
 *
 * @author _Klaro | Pasqual K. / created on 16.04.2019
 */

public final class OutgoingPacketEvent extends Event implements Serializable, Cancellable {
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
     * Creates a new packet event
     *
     * @param awaitingPacket        The packet which should be sent
     */
    public OutgoingPacketEvent(AwaitingPacket awaitingPacket) {
        this.awaitingPacket = awaitingPacket;
    }


    /**
     * The packet which should be sent
     */
    @Getter
    private AwaitingPacket awaitingPacket;
}
