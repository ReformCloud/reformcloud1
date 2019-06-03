/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.player.implementations;

import systems.reformcloud.player.DefaultPlayer;
import systems.reformcloud.player.version.SpigotVersion;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 08.03.2019
 */

public final class OfflinePlayer extends DefaultPlayer implements Serializable {

    public OfflinePlayer(String name, UUID uniqueID, Map<String, Object> playerMeta, long lastLogin,
        SpigotVersion lastSpigotVersion) {
        super(name, uniqueID, playerMeta, lastLogin, lastSpigotVersion);
    }
}
