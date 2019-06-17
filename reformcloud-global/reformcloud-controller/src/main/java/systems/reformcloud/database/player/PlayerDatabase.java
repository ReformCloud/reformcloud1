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
import systems.reformcloud.utility.annotiations.MayNotBePresent;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author _Klaro | Pasqual K. / created on 08.03.2019
 */

public final class PlayerDatabase extends DatabaseProvider implements SavePlayerDatabase, Serializable {

    private Cache<UUID, OfflinePlayer> cachedOfflinePlayers = ReformCloudLibraryService
        .newCache(1000);

    private Map<UUID, OnlinePlayer> cachedOnlinePlayers = new HashMap<>();

    private final File dir = new File("reformcloud/database/players");

    private final File nameToUuid = new File("reformcloud/database/players/nametouuid");

    private final Lock writeLock = new ReentrantLock();

    @Override
    @MayNotBePresent
    public OfflinePlayer getOfflinePlayer(DefaultPlayer defaultPlayer) {
        if (this.cachedOfflinePlayers.contains(defaultPlayer.getUniqueID())
            && this.cachedOfflinePlayers.getSave(defaultPlayer.getUniqueID()).isPresent()) {
            return this.cachedOfflinePlayers.getSave(defaultPlayer.getUniqueID()).get();
        }

        if (dir.isDirectory()) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                if (file.getName().endsWith(".json") && file.getName().replace(".json", "")
                    .equals(String.valueOf(defaultPlayer.getUniqueID()))) {
                    try {
                        Configuration configuration = Configuration.parse(file);
                        OfflinePlayer offlinePlayer = configuration
                            .getValue("player", TypeTokenAdaptor.getOFFLINE_PLAYER_TYPE());

                        assert offlinePlayer != null;

                        offlinePlayer.setLastLogin(defaultPlayer.getLastLogin());
                        offlinePlayer.setSpigotVersion(defaultPlayer.getSpigotVersion());
                        offlinePlayer.setName(defaultPlayer.getName());

                        this.updateOfflinePlayer(offlinePlayer);
                        return offlinePlayer;
                    } catch (final Throwable throwable) {
                        StringUtil.printError(
                            ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                            "Could not load OfflinePlayer", throwable);
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

        writeLock.lock();

        try {
            saveOfflinePlayer0(defaultPlayer, offlinePlayer);
        } finally {
            writeLock.unlock();
        }

        return offlinePlayer;
    }

    @Override
    @MayNotBePresent
    public OfflinePlayer getOfflinePlayer(UUID uuid) {
        if (this.cachedOfflinePlayers.contains(uuid)
            && this.cachedOfflinePlayers.getSave(uuid).isPresent()) {
            return this.cachedOfflinePlayers.getSave(uuid).get();
        }

        if (dir.isDirectory()) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                if (file.getName().endsWith(".json") && file.getName().replace(".json", "")
                    .equals(String.valueOf(uuid))) {
                    try {
                        Configuration configuration = Configuration.parse(file);
                        OfflinePlayer offlinePlayer = configuration
                            .getValue("player", TypeTokenAdaptor.getOFFLINE_PLAYER_TYPE());
                        assert offlinePlayer != null;
                        this.cachedOfflinePlayers.add(offlinePlayer.getUniqueID(), offlinePlayer);
                        return offlinePlayer;
                    } catch (final Throwable throwable) {
                        StringUtil.printError(
                            ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                            "Could not load OfflinePlayer", throwable);
                        return null;
                    }
                }
            }
        }

        return null;
    }

    @Override
    @MayNotBePresent
    public UUID getFromName(String name) {
        Optional<OfflinePlayer> offlinePlayer = this.cachedOfflinePlayers.asMap()
            .values().stream().filter(e -> e.getName().equals(name)).findFirst();
        if (offlinePlayer.isPresent()) {
            return offlinePlayer.get().getUniqueID();
        }

        if (nameToUuid.isDirectory()) {
            for (File file : Objects.requireNonNull(nameToUuid.listFiles())) {
                if (file.getName().endsWith(".json")
                    && file.getName().replace(".json", "").equals(name)) {
                    try {
                        Configuration configuration = Configuration.parse(file);
                        return configuration.getValue("uuid", UUID.class);
                    } catch (final Throwable throwable) {
                        StringUtil.printError(
                            ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                            "Could not load uuid", throwable);
                        return null;
                    }
                }
            }
        }

        return null;
    }

    @Override
    @MayNotBePresent
    public String getFromUUID(UUID uuid) {
        OfflinePlayer offlinePlayer = this.getOfflinePlayer(uuid);
        return offlinePlayer == null ? null : offlinePlayer.getName();
    }

    public OnlinePlayer getOnlinePlayer(UUID uuid) {
        return this.cachedOnlinePlayers.get(uuid);
    }

    @Override
    public void cacheOnlinePlayer(OnlinePlayer onlinePlayer) {
        if (!this.cachedOnlinePlayers.containsKey(onlinePlayer.getUniqueID())) {
            this.cachedOnlinePlayers.put(onlinePlayer.getUniqueID(), onlinePlayer);
        }
    }

    @Override
    public void logoutPlayer(UUID uuid) {
        this.cachedOfflinePlayers.invalidate(uuid);
        this.cachedOnlinePlayers.remove(uuid);
    }

    @Override
    public void updateOnlinePlayer(OnlinePlayer onlinePlayer) {
        if (!this.cachedOnlinePlayers.containsKey(onlinePlayer.getUniqueID())) {
            this.cacheOnlinePlayer(onlinePlayer);
            return;
        }

        this.cachedOnlinePlayers.remove(onlinePlayer.getUniqueID());
        this.cachedOnlinePlayers.put(onlinePlayer.getUniqueID(), onlinePlayer);
    }

    @Override
    public void updateOfflinePlayer(OfflinePlayer offlinePlayer) {
        writeLock.lock();

        try {
            updateOfflinePlayer0(offlinePlayer);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Map<UUID, OnlinePlayer> cachedOnlinePlayers() {
        return cachedOnlinePlayers;
    }

    private void updateOfflinePlayer0(OfflinePlayer offlinePlayer) {
        new Configuration()
            .addValue("player", offlinePlayer)
            .write(Paths.get(dir + "/" + offlinePlayer.getUniqueID() + ".json"));
        this.cachedOfflinePlayers.invalidate(offlinePlayer.getUniqueID());
        this.cachedOfflinePlayers.add(offlinePlayer.getUniqueID(), offlinePlayer);
    }

    private void saveOfflinePlayer0(DefaultPlayer defaultPlayer,
                                    OfflinePlayer offlinePlayer) {
        new Configuration().addValue("player", offlinePlayer)
            .write(Paths.get(dir + "/" + defaultPlayer.getUniqueID() + ".json"));
        new Configuration().addValue("uuid", offlinePlayer.getUniqueID())
            .write(Paths.get(nameToUuid + "/" +
                defaultPlayer.getName() + ".json"));
        this.cachedOfflinePlayers.add(defaultPlayer.getUniqueID(), offlinePlayer);
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
