/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database.player;

import systems.reformcloud.player.DefaultPlayer;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.player.implementations.OnlinePlayer;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 17.06.2019
 */

public interface SavePlayerDatabase extends Serializable {

    OfflinePlayer getOfflinePlayer(DefaultPlayer arg1);

    OfflinePlayer getOfflinePlayer(UUID arg1);

    UUID getFromName(String arg1);

    String getFromUUID(UUID arg1);

    OnlinePlayer getOnlinePlayer(UUID arg1);

    void cacheOnlinePlayer(OnlinePlayer arg1);

    void logoutPlayer(UUID arg1);

    void updateOnlinePlayer(OnlinePlayer arg1);

    void updateOfflinePlayer(OfflinePlayer arg1);

    Map<UUID, OnlinePlayer> cachedOnlinePlayers();
}
