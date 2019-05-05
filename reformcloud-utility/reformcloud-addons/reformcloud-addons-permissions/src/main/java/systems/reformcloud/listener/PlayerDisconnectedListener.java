/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.listener;

import systems.reformcloud.PermissionsAddon;
import systems.reformcloud.event.annotation.Handler;
import systems.reformcloud.event.events.PlayerDisconnectsEvent;
import systems.reformcloud.event.utility.Listener;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 05.05.2019
 */

public final class PlayerDisconnectedListener implements Serializable, Listener {
    @Handler
    public void handle(final PlayerDisconnectsEvent event) {
        PermissionsAddon.getInstance().getPermissionDatabase().getCachedPermissionHolders().remove(event.getOnlinePlayer());
    }
}
