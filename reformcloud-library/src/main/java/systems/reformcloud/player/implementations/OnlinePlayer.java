/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.player.implementations;

import systems.reformcloud.player.DefaultPlayer;
import systems.reformcloud.player.version.SpigotVersion;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 08.03.2019
 */

public final class OnlinePlayer extends DefaultPlayer implements Serializable {
    public OnlinePlayer(String name, UUID uniqueID, SpigotVersion spigotVersion, String currentServer, String currentProxy) {
        super(name, uniqueID, new HashMap<>(), System.currentTimeMillis(), spigotVersion);
        this.currentProxy = currentProxy;
        this.currentServer = currentServer;
    }

    private String currentServer, currentProxy;

    public String getCurrentServer() {
        return this.currentServer;
    }

    public String getCurrentProxy() {
        return this.currentProxy;
    }

    public void setCurrentServer(String currentServer) {
        this.currentServer = currentServer;
    }

    public void setCurrentProxy(String currentProxy) {
        this.currentProxy = currentProxy;
    }
}
