/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events;

import systems.reformcloud.event.utility.Cancellable;
import systems.reformcloud.event.utility.Event;
import systems.reformcloud.player.implementations.OfflinePlayer;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 17.04.2019
 */

public final class OfflinePlayerUpdateEvent extends Event implements Serializable, Cancellable {

    private boolean cancelled;

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public OfflinePlayerUpdateEvent(OfflinePlayer oldPlayer, OfflinePlayer newPlayer) {
        this.oldPlayer = oldPlayer;
        this.newPlayer = newPlayer;
    }

    private OfflinePlayer oldPlayer, newPlayer;

    public OfflinePlayer getOldPlayer() {
        return this.oldPlayer;
    }

    public OfflinePlayer getNewPlayer() {
        return this.newPlayer;
    }
}
