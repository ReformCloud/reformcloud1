/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events;

import systems.reformcloud.event.utility.Event;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 17.04.2019
 */

public final class PlayerDisconnectsEvent extends Event implements Serializable {
    private UUID onlinePlayer;

    @java.beans.ConstructorProperties({"onlinePlayer"})
    public PlayerDisconnectsEvent(UUID onlinePlayer) {
        this.onlinePlayer = onlinePlayer;
    }

    public UUID getOnlinePlayer() {
        return this.onlinePlayer;
    }
}
