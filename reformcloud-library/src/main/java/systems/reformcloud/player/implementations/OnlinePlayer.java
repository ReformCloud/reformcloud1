/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.player.implementations;

import lombok.Getter;
import lombok.Setter;
import systems.reformcloud.player.DefaultPlayer;
import systems.reformcloud.player.version.SpigotVersion;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 08.03.2019
 */

@Getter
public final class OnlinePlayer extends DefaultPlayer implements Serializable {
    public OnlinePlayer(String name, UUID uniqueID, SpigotVersion spigotVersion, String currentServer, String currentProxy) {
        super(name, uniqueID, new HashMap<>(), System.currentTimeMillis(), spigotVersion);
        this.currentProxy = currentProxy;
        this.currentServer = currentServer;
    }

    @Setter
    private String currentServer, currentProxy;
}
