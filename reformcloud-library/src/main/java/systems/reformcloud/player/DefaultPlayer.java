/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.player;

import systems.reformcloud.player.version.SpigotVersion;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 22.02.2019
 */

public class DefaultPlayer implements Serializable {
    private static final long serialVersionUID = -6988920422303669100L;

    private String name;

    private UUID uniqueID;
    private Map<String, Object> playerMeta;

    private long lastLogin;

    private SpigotVersion spigotVersion;

    @java.beans.ConstructorProperties({"name", "uniqueID", "playerMeta", "lastLogin", "spigotVersion"})
    public DefaultPlayer(String name, UUID uniqueID, Map<String, Object> playerMeta, long lastLogin, SpigotVersion spigotVersion) {
        this.name = name;
        this.uniqueID = uniqueID;
        this.playerMeta = playerMeta;
        this.lastLogin = lastLogin;
        this.spigotVersion = spigotVersion;
    }

    public String getName() {
        return this.name;
    }

    public UUID getUniqueID() {
        return this.uniqueID;
    }

    public Map<String, Object> getPlayerMeta() {
        return this.playerMeta;
    }

    public long getLastLogin() {
        return this.lastLogin;
    }

    public SpigotVersion getSpigotVersion() {
        return this.spigotVersion;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setSpigotVersion(SpigotVersion spigotVersion) {
        this.spigotVersion = spigotVersion;
    }
}
