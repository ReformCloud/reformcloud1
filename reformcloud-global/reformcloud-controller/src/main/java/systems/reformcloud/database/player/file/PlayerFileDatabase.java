/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database.player.file;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.database.Database;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.utility.TypeTokenAdaptor;
import systems.reformcloud.utility.files.FileUtils;
import systems.reformcloud.utility.future.DefaultTask;
import systems.reformcloud.utility.future.Task;
import systems.reformcloud.utility.future.TaskListener;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * @author _Klaro | Pasqual K. / created on 11.08.2019
 */

public final class PlayerFileDatabase extends Database<UUID, String, OfflinePlayer> implements Serializable {

    private static final File DATABASE_DIRECTORY = new File("reformcloud/database/players");

    private static final File NAME_TO_UUID_DATABASE_DIRECTORY = new File("reformcloud/database/players/nametouuid");

    private final Lock writeLock = new ReentrantLock();

    @Override
    public OfflinePlayer insert(UUID key, OfflinePlayer value) {
        return insertAsync(key, value).addListener(new TaskListener<OfflinePlayer>() {
            @Override
            public void onTimeOut() {
                onOperationFailed(new TimeoutException());
            }

            @Override
            public void onOperationFailed(Throwable cause) {
                FileDatabaseManager.handleError(cause);
            }
        }).execute(TimeUnit.SECONDS, 5);
    }

    @Override
    public Task<OfflinePlayer> insertAsync(UUID key, OfflinePlayer value) {
        Task<OfflinePlayer> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            try {
                writeLock.lock();

                if (DATABASE_DIRECTORY.isDirectory()) {
                    File[] users = DATABASE_DIRECTORY.listFiles(pathname -> pathname.getName().endsWith(".json")
                        && pathname.getName().replace(".json", "").equals(key.toString()));
                    if (users != null && users.length != 0) {
                        File user = users[0];
                        Configuration configuration = Configuration.parse(user);
                        task.complete(configuration.getValue("player", TypeTokenAdaptor.getOFFLINE_PLAYER_TYPE()));
                    } else {
                        Configuration configuration = new Configuration();
                        configuration.addValue("player", value).write(Paths.get(
                            MessageFormat.format(
                                "{0}/{1}.{2}",
                                DATABASE_DIRECTORY.toString(),
                                key.toString(),
                                "json"
                            )
                        ));

                        Configuration configuration1 = new Configuration();
                        configuration1.addValue("uuid", key).write(Paths.get(
                            MessageFormat.format(
                                "{0}/{1}.{2}",
                                NAME_TO_UUID_DATABASE_DIRECTORY.toString(),
                                value.getName(),
                                "json"
                            )
                        ));

                        task.complete(value);
                    }
                }
            } finally {
                writeLock.unlock();
            }
        });
        return task;
    }

    @Override
    public Boolean remove(UUID key) {
        return removeAsync(key).addListener(new TaskListener<Boolean>() {
            @Override
            public void onTimeOut() {
                onOperationFailed(new TimeoutException());
            }

            @Override
            public void onOperationFailed(Throwable cause) {
                FileDatabaseManager.handleError(cause);
            }
        }).execute(TimeUnit.SECONDS, 5);
    }

    @Override
    public Task<Boolean> removeAsync(UUID key) {
        Task<Boolean> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            File[] users = DATABASE_DIRECTORY.listFiles(pathname -> pathname.getName().endsWith(".json")
                && pathname.getName().replace(".json", "").equals(key.toString()));
            if (users != null && users.length != 0) {
                OfflinePlayer offlinePlayer = get(key);
                FileUtils.deleteFileIfExists(users[0]);
                FileUtils.deleteFileIfExists(Paths.get(
                    MessageFormat.format(
                        "{0}/{1}.{2}",
                        NAME_TO_UUID_DATABASE_DIRECTORY.toString(),
                        offlinePlayer.getName(),
                        ".json"
                    )
                ));
                task.complete(true);
            } else {
                task.complete(false);
            }
        });
        return task;
    }

    @Override
    public Boolean update(UUID key, OfflinePlayer newValue) {
        return updateAsync(key, newValue).addListener(new TaskListener<Boolean>() {
            @Override
            public void onTimeOut() {
                onOperationFailed(new TimeoutException());
            }

            @Override
            public void onOperationFailed(Throwable cause) {
                FileDatabaseManager.handleError(cause);
            }
        }).execute(TimeUnit.SECONDS, 5);
    }

    @Override
    public Task<Boolean> updateAsync(UUID key, OfflinePlayer newValue) {
        Task<Boolean> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            if (PlayerFileDatabase.this.remove(key)) {
                PlayerFileDatabase.this.insert(key, newValue);
                task.complete(true);
            } else {
                task.complete(false);
            }
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
            File[] users = DATABASE_DIRECTORY.listFiles(pathname -> pathname.getName().endsWith(".json")
                && pathname.getName().replace(".json", "").equals(key.toString()));
            task.complete(users != null && users.length != 0);
        });
        return task;
    }

    @Override
    public OfflinePlayer get(UUID key) {
        return getAsync(key).addListener(new TaskListener<OfflinePlayer>() {
            @Override
            public void onTimeOut() {
                onOperationFailed(new TimeoutException());
            }

            @Override
            public void onOperationFailed(Throwable cause) {
                FileDatabaseManager.handleError(cause);
            }
        }).execute(TimeUnit.SECONDS, 5);
    }

    @Override
    public Task<OfflinePlayer> getAsync(UUID key) {
        Task<OfflinePlayer> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            File[] users = DATABASE_DIRECTORY.listFiles(pathname -> pathname.getName().endsWith(".json")
                && pathname.getName().replace(".json", "").equals(key.toString()));
            if (users != null && users.length != 0) {
                File user = users[0];
                Configuration configuration = Configuration.parse(user);
                task.complete(configuration.getValue("player", TypeTokenAdaptor.getOFFLINE_PLAYER_TYPE()));
            } else {
                task.complete(null);
            }
        });
        return task;
    }

    @Override
    public UUID getID(String identifier) {
        return getIDAsync(identifier).addListener(new TaskListener<UUID>() {
            @Override
            public void onTimeOut() {
                onOperationFailed(new TimeoutException());
            }

            @Override
            public void onOperationFailed(Throwable cause) {
                FileDatabaseManager.handleError(cause);
            }
        }).execute(TimeUnit.SECONDS, 5);
    }

    @Override
    public Task<UUID> getIDAsync(String identifier) {
        Task<UUID> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            File[] users = NAME_TO_UUID_DATABASE_DIRECTORY.listFiles(pathname -> pathname.getName().endsWith(".json")
                && pathname.getName().replace(".json", "").equals(identifier));
            if (users != null && users.length != 0) {
                Configuration configuration = Configuration.parse(users[0]);
                task.complete(configuration.getValue("uuid", UUID.class));
            } else {
                task.complete(null);
            }
        });
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
            File[] files = DATABASE_DIRECTORY.listFiles(pathname -> pathname.getName().endsWith(".json"));
            if (files != null && files.length != 0) {
                Arrays.stream(files).forEach(file -> {
                    Configuration configuration = Configuration.parse(file);
                    consumer.accept(configuration.getValue("player", TypeTokenAdaptor.getOFFLINE_PLAYER_TYPE()));
                });
            }

            task.complete(null);
        });
        return task;
    }
}
