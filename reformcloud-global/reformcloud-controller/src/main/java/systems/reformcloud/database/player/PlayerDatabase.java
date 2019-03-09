/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database.player;

import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.database.DatabaseProvider;
import systems.reformcloud.player.DefaultPlayer;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.player.implementations.OnlinePlayer;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 08.03.2019
 */

public final class PlayerDatabase extends DatabaseProvider implements Serializable {
    private Map<UUID, OfflinePlayer> cachedOfflinePlayers = new HashMap<>();
    private Map<UUID, OnlinePlayer> cachedOnlinePlayers = new HashMap<>();

    private final File dir = new File("reformcloud/database/players");
    private final File name_to_uuid = new File("reformcloud/database/players/nametouuid");

    public OfflinePlayer getOfflinePlayer(DefaultPlayer defaultPlayer) {
        if (this.cachedOfflinePlayers.containsKey(defaultPlayer.getUniqueID()))
            return this.cachedOfflinePlayers.get(defaultPlayer.getUniqueID());

        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.getName().endsWith(".json") && file.getName().replace(".json", "")
                        .equals(String.valueOf(defaultPlayer.getUniqueID()))) {
                    try {
                        Configuration configuration = Configuration.parse(file);
                        OfflinePlayer offlinePlayer = configuration.getValue("player", TypeTokenAdaptor.getOFFLINE_PLAYER_TYPE());

                        offlinePlayer.setLastLogin(defaultPlayer.getLastLogin());
                        offlinePlayer.setSpigotVersion(defaultPlayer.getSpigotVersion());
                        offlinePlayer.setName(defaultPlayer.getName());

                        this.updateOfflinePlayer(offlinePlayer);
                        return offlinePlayer;
                    } catch (final Throwable throwable) {
                        StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not load OfflinePlayer", throwable);
                        return null;
                    }
                }
            }
        }

        OfflinePlayer offlinePlayer = new OfflinePlayer(
                defaultPlayer.getName(),
                defaultPlayer.getUniqueID(),
                new HashMap<>(),
                defaultPlayer.getLastLogin(),
                defaultPlayer.getSpigotVersion()
        );

        new Configuration().addProperty("player", offlinePlayer).write(Paths.get(dir + "/" + defaultPlayer.getUniqueID() + ".json"));
        new Configuration().addProperty("uuid", offlinePlayer.getUniqueID()).write(Paths.get(name_to_uuid + "/" +
                defaultPlayer.getName() + ".json"));
        this.cachedOfflinePlayers.put(defaultPlayer.getUniqueID(), offlinePlayer);
        return offlinePlayer;
    }

    public OfflinePlayer getOfflinePlayer(UUID uuid) {
        if (this.cachedOfflinePlayers.containsKey(uuid))
            return this.cachedOfflinePlayers.get(uuid);

        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.getName().endsWith(".json") && file.getName().replace(".json", "")
                        .equals(String.valueOf(uuid))) {
                    try {
                        Configuration configuration = Configuration.parse(file);
                        OfflinePlayer offlinePlayer = configuration.getValue("player", TypeTokenAdaptor.getOFFLINE_PLAYER_TYPE());
                        this.cachedOfflinePlayers.put(offlinePlayer.getUniqueID(), offlinePlayer);
                        return offlinePlayer;
                    } catch (final Throwable throwable) {
                        StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not load OfflinePlayer", throwable);
                        return null;
                    }
                }
            }
        }

        return null;
    }

    public UUID getFromName(String name) {
        Optional<OfflinePlayer> offlinePlayer = this.cachedOfflinePlayers
                .values().stream().filter(e -> e.getName().equals(name)).findFirst();
        if (offlinePlayer.isPresent())
            return offlinePlayer.get().getUniqueID();

        if (name_to_uuid.isDirectory()) {
            for (File file : name_to_uuid.listFiles()) {
                if (file.getName().endsWith(".json")
                        && file.getName().replace(".json", "").equals(name)) {
                    try {
                        Configuration configuration = Configuration.parse(file);
                        return configuration.getValue("uuid", UUID.class);
                    } catch (final Throwable throwable) {
                        StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not load uuid", throwable);
                        return null;
                    }
                }
            }
        }

        return null;
    }

    public OnlinePlayer getOnlinePlayer(UUID uuid) {
        return this.cachedOnlinePlayers.get(uuid);
    }

    public void cacheOfflinePlayer(OfflinePlayer offlinePlayer) {
        if (!this.cachedOfflinePlayers.containsKey(offlinePlayer.getUniqueID()))
            this.cachedOfflinePlayers.put(offlinePlayer.getUniqueID(), offlinePlayer);
    }

    public void cacheOnlinePlayer(OnlinePlayer onlinePlayer) {
        if (!this.cachedOnlinePlayers.containsKey(onlinePlayer.getUniqueID()))
            this.cachedOnlinePlayers.put(onlinePlayer.getUniqueID(), onlinePlayer);
    }

    public void logoutPlayer(UUID uuid) {
        this.cachedOnlinePlayers.remove(uuid);
    }

    public void updateOnlinePlayer(OnlinePlayer onlinePlayer) {
        if (!this.cachedOnlinePlayers.containsKey(onlinePlayer.getUniqueID())) {
            this.cacheOnlinePlayer(onlinePlayer);
            return;
        }

        this.cachedOnlinePlayers.remove(onlinePlayer.getUniqueID());
        this.cachedOnlinePlayers.put(onlinePlayer.getUniqueID(), onlinePlayer);
    }

    public void updateOfflinePlayer(OfflinePlayer offlinePlayer) {
        new Configuration()
                .addProperty("player", offlinePlayer)
                .write(Paths.get(dir + "/" + offlinePlayer.getUniqueID() + ".json"));
        this.cachedOfflinePlayers.remove(offlinePlayer.getUniqueID());
        this.cachedOfflinePlayers.put(offlinePlayer.getUniqueID(), offlinePlayer);
    }

    @Override
    public String getName() {
        return "PlayerDatabase";
    }

    @Override
    public void load() {
    }

    @Override
    public void save() {
    }
}
