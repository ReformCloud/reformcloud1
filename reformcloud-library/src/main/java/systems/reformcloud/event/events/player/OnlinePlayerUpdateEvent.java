/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.player;

import systems.reformcloud.event.utility.Cancellable;
import systems.reformcloud.event.utility.Event;
import systems.reformcloud.player.implementations.OnlinePlayer;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 17.04.2019
 */

public final class OnlinePlayerUpdateEvent extends Event implements Serializable, Cancellable {

    private boolean cancelled;

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public OnlinePlayerUpdateEvent(OnlinePlayer oldPlayer, OnlinePlayer newPlayer) {
        this.oldPlayer = oldPlayer;
        this.newPlayer = newPlayer;
    }

    private OnlinePlayer oldPlayer, newPlayer;

    public OnlinePlayer getOldPlayer() {
        return this.oldPlayer;
    }

    public OnlinePlayer getNewPlayer() {
        return this.newPlayer;
    }
}
