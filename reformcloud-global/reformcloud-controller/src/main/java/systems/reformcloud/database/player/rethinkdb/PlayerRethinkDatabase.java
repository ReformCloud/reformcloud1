/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database.player.rethinkdb;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import systems.reformcloud.database.Database;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.utility.future.DefaultTask;
import systems.reformcloud.utility.future.Task;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author _Klaro | Pasqual K. / created on 12.08.2019
 */

public final class PlayerRethinkDatabase extends Database<UUID, String, OfflinePlayer> implements Serializable {

    private static final String TABLE_NAME = "players";

    private static final String NAME_TO_UUID_TABLE = "nameToUUID";

    private static final RethinkDB RETHINK_DB = RethinkDB.r;

    public PlayerRethinkDatabase(Connection connection) {
        this.connection = connection;

        RETHINK_DB.tableCreate(TABLE_NAME).run(connection);
        RETHINK_DB.tableCreate(NAME_TO_UUID_TABLE).run(connection);
    }

    private final Connection connection;

    @Override
    public OfflinePlayer insert(UUID key, OfflinePlayer value) {
        return insertAsync(key, value).execute();
    }

    @Override
    public Task<OfflinePlayer> insertAsync(UUID key, OfflinePlayer value) {
        Task<OfflinePlayer> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            OfflinePlayer offlinePlayer = RETHINK_DB.table(TABLE_NAME).get(key).run(connection);
            if (offlinePlayer == null) {
                RETHINK_DB.table(TABLE_NAME).insert(RETHINK_DB.hashMap("key", key).with("player", value)).run(connection);
                RETHINK_DB.table(NAME_TO_UUID_TABLE).insert(RETHINK_DB.hashMap("key", offlinePlayer.getName()).with("uuid", key)).run(connection);
                task.complete(value);
            } else {
                task.complete(offlinePlayer);
            }
        });
        return task;
    }

    @Override
    public Boolean remove(UUID key) {
        return removeAsync(key).execute();
    }

    @Override
    public Task<Boolean> removeAsync(UUID key) {
        Task<Boolean> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            RETHINK_DB.table(TABLE_NAME).get(key).delete().run(connection);
            task.complete(true);
        });
        return task;
    }

    @Override
    public Boolean update(UUID key, OfflinePlayer newValue) {
        return updateAsync(key, newValue).execute();
    }

    @Override
    public Task<Boolean> updateAsync(UUID key, OfflinePlayer newValue) {
        Task<Boolean> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            RETHINK_DB.table(TABLE_NAME).get(key).update(RETHINK_DB.hashMap("player", newValue)).run(connection);
            task.complete(true);
        });
        return task;
    }

    @Override
    public Boolean contains(UUID key) {
        return containsAsync(key).execute();
    }

    @Override
    public Task<Boolean> containsAsync(UUID key) {
        Task<Boolean> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            OfflinePlayer offlinePlayer = RETHINK_DB.table(TABLE_NAME).get(key).run(connection);
            task.complete(offlinePlayer != null);
        });
        return task;
    }

    @Override
    public OfflinePlayer get(UUID key) {
        return getAsync(key).execute();
    }

    @Override
    public Task<OfflinePlayer> getAsync(UUID key) {
        Task<OfflinePlayer> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> task.complete(RETHINK_DB.table(TABLE_NAME).get(key).run(connection)));
        return task;
    }

    @Override
    public UUID getID(String identifier) {
        return getIDAsync(identifier).execute();
    }

    @Override
    public Task<UUID> getIDAsync(String identifier) {
        Task<UUID> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> task.complete(RETHINK_DB.table(NAME_TO_UUID_TABLE).get(identifier).run(connection)));
        return task;
    }

    @Override
    public Void forEach(Consumer<OfflinePlayer> consumer) {
        return forEachAsync(consumer).execute(TimeUnit.MINUTES, 5);
    }

    @Override
    public Task<Void> forEachAsync(Consumer<OfflinePlayer> consumer) {
        Task<Void> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            Cursor<UUID> cursor = RETHINK_DB.table(TABLE_NAME).run(connection);
            cursor.forEach(uuid -> consumer.accept(get(uuid)));
            task.complete(null);
        });
        return task;
    }
}
