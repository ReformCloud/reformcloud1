/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.player;

import systems.reformcloud.player.implementations.OnlinePlayer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 11.08.2019
 */

public final class OnlinePlayerManager implements Serializable {

    public static final OnlinePlayerManager INTERNAL_INSTANCE = new OnlinePlayerManager();

    private final Map<UUID, OnlinePlayer> onlinePlayers = new HashMap<>();

    public void loginPlayer(OnlinePlayer onlinePlayer) {
        onlinePlayers.put(onlinePlayer.getUniqueID(), onlinePlayer);
    }

    public OnlinePlayer get(UUID key) {
        return onlinePlayers.get(key);
    }

    public void updatePlayer(OnlinePlayer onlinePlayer) {
        this.onlinePlayers.put(onlinePlayer.getUniqueID(), onlinePlayer);
    }

    public void logoutPlayer(UUID key) {
        onlinePlayers.remove(key);
    }
}
