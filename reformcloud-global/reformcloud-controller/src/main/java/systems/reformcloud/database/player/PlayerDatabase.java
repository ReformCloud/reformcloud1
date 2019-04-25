/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database.player;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.cache.Cache;
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
import java.util.Optional;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 08.03.2019
 */

public final class PlayerDatabase extends DatabaseProvider implements Serializable {
    private Cache<UUID, OfflinePlayer> cachedOfflinePlayers = ReformCloudLibraryService.newCache(1000);
    public Cache<UUID, OnlinePlayer> cachedOnlinePlayers = ReformCloudLibraryService.newCache(1000);

    private final File dir = new File("reformcloud/database/players");
    private final File name_to_uuid = new File("reformcloud/database/players/nametouuid");

    public OfflinePlayer getOfflinePlayer(DefaultPlayer defaultPlayer) {
        if (this.cachedOfflinePlayers.contains(defaultPlayer.getUniqueID())
                && this.cachedOfflinePlayers.getSave(defaultPlayer.getUniqueID()).orElse(null) != null) {
            return this.cachedOfflinePlayers.getSave(defaultPlayer.getUniqueID()).get();
        }

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

        new Configuration().addValue("player", offlinePlayer).write(Paths.get(dir + "/" + defaultPlayer.getUniqueID() + ".json"));
        new Configuration().addValue("uuid", offlinePlayer.getUniqueID()).write(Paths.get(name_to_uuid + "/" +
                defaultPlayer.getName() + ".json"));
        this.cachedOfflinePlayers.add(defaultPlayer.getUniqueID(), offlinePlayer);
        return offlinePlayer;
    }

    public OfflinePlayer getOfflinePlayer(UUID uuid) {
        if (this.cachedOfflinePlayers.contains(uuid)
                && this.cachedOfflinePlayers.getSave(uuid).orElse(null) != null) {
            return this.cachedOfflinePlayers.getSave(uuid).get();
        }

        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.getName().endsWith(".json") && file.getName().replace(".json", "")
                        .equals(String.valueOf(uuid))) {
                    try {
                        Configuration configuration = Configuration.parse(file);
                        OfflinePlayer offlinePlayer = configuration.getValue("player", TypeTokenAdaptor.getOFFLINE_PLAYER_TYPE());
                        this.cachedOfflinePlayers.add(offlinePlayer.getUniqueID(), offlinePlayer);
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
        Optional<OfflinePlayer> offlinePlayer = this.cachedOfflinePlayers.asMap()
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
        return this.cachedOnlinePlayers.getSave(uuid).orElse(null);
    }

    public void cacheOfflinePlayer(OfflinePlayer offlinePlayer) {
        if (!this.cachedOfflinePlayers.contains(offlinePlayer.getUniqueID()))
            this.cachedOfflinePlayers.add(offlinePlayer.getUniqueID(), offlinePlayer);
    }

    public void cacheOnlinePlayer(OnlinePlayer onlinePlayer) {
        if (!this.cachedOnlinePlayers.contains(onlinePlayer.getUniqueID()))
            this.cachedOnlinePlayers.add(onlinePlayer.getUniqueID(), onlinePlayer);
    }

    public void logoutPlayer(UUID uuid) {
        this.cachedOfflinePlayers.invalidate(uuid);
        this.cachedOnlinePlayers.invalidate(uuid);
    }

    public void updateOnlinePlayer(OnlinePlayer onlinePlayer) {
        if (!this.cachedOnlinePlayers.contains(onlinePlayer.getUniqueID())) {
            this.cacheOnlinePlayer(onlinePlayer);
            return;
        }

        this.cachedOnlinePlayers.invalidate(onlinePlayer.getUniqueID());
        this.cachedOnlinePlayers.add(onlinePlayer.getUniqueID(), onlinePlayer);
    }

    public void updateOfflinePlayer(OfflinePlayer offlinePlayer) {
        new Configuration()
                .addValue("player", offlinePlayer)
                .write(Paths.get(dir + "/" + offlinePlayer.getUniqueID() + ".json"));
        this.cachedOfflinePlayers.invalidate(offlinePlayer.getUniqueID());
        this.cachedOfflinePlayers.add(offlinePlayer.getUniqueID(), offlinePlayer);
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
