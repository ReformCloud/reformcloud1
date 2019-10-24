/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database.player.mysql;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.database.Database;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.utility.TypeTokenAdaptor;
import systems.reformcloud.utility.future.DefaultTask;
import systems.reformcloud.utility.future.Task;
import systems.reformcloud.utility.future.TaskListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public final class PlayerMySQLDatabase extends Database<UUID, String, OfflinePlayer> implements Serializable {

    private static final String TABLE_NAME = "players";

    public PlayerMySQLDatabase(MySQLDatabaseManager mySQLDatabaseManager) {
        this.mySQLDatabaseManager = mySQLDatabaseManager;
        try {
            PreparedStatement preparedStatement = mySQLDatabaseManager.connection.get()
                .prepareStatement("CREATE TABLE IF NOT EXISTS `" + TABLE_NAME + "` (`uuid` VARCHAR(64), `name` VARCHAR(16), `value` LONGBLOB);");
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (final SQLException ex) {
            MySQLDatabaseManager.handleError(ex);
        }
    }

    private final MySQLDatabaseManager mySQLDatabaseManager;

    @Override
    public OfflinePlayer insert(UUID key, OfflinePlayer value) {
        return insertAsync(key, value).execute();
    }

    @Override
    public Task<OfflinePlayer> insertAsync(UUID key, OfflinePlayer value) {
        Task<OfflinePlayer> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            try {
                PreparedStatement preparedStatement = mySQLDatabaseManager.connection.get()
                    .prepareStatement("INSERT INTO `" + TABLE_NAME + "` (`uuid`, `name`, `value`) VALUES (?, ? ,?);");
                preparedStatement.setString(1, key.toString());
                preparedStatement.setString(2, value.getName());
                preparedStatement.setBytes(3, convert(value));
                preparedStatement.executeUpdate();
                preparedStatement.close();

                task.complete(value);
            } catch (final SQLException ex) {
                MySQLDatabaseManager.handleError(ex);
            }

            task.complete(null);
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
            try {
                PreparedStatement preparedStatement = this.mySQLDatabaseManager.connection.get()
                    .prepareStatement("DELETE FROM `" + this.TABLE_NAME + "` WHERE `uuid` = ?");
                preparedStatement.setString(1, key.toString());
                preparedStatement.executeUpdate();
                preparedStatement.close();
                task.complete(true);
            } catch (final SQLException ex) {
                MySQLDatabaseManager.handleError(ex);
                task.complete(false);
            }
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
            try {
                PreparedStatement preparedStatement = this.mySQLDatabaseManager.connection.get()
                    .prepareStatement("UPDATE `" + this.TABLE_NAME + "` SET `value` = ? WHERE `uuid` = ?");
                preparedStatement.setBytes(1, convert(newValue));
                preparedStatement.setString(2, key.toString());
                preparedStatement.executeUpdate();
                preparedStatement.close();
                task.complete(true);
            } catch (final SQLException ex) {
                MySQLDatabaseManager.handleError(ex);
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
            try {
                PreparedStatement preparedStatement = this.mySQLDatabaseManager.connection.get()
                    .prepareStatement("SELECT `uuid` FROM `" + this.TABLE_NAME + "` WHERE `uuid` = ?");
                preparedStatement.setString(1, key.toString());
                ResultSet resultSet = preparedStatement.executeQuery();
                task.complete(resultSet.next());
                preparedStatement.close();
                resultSet.close();
            } catch (final SQLException ex) {
                MySQLDatabaseManager.handleError(ex);
                task.complete(false);
            }
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
                MySQLDatabaseManager.handleError(cause);
            }
        }).execute(TimeUnit.SECONDS, 5);
    }

    @Override
    public Task<OfflinePlayer> getAsync(UUID key) {
        Task<OfflinePlayer> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            try {
                PreparedStatement preparedStatement = this.mySQLDatabaseManager.connection.get()
                    .prepareStatement("SELECT `value` FROM `" + this.TABLE_NAME + "` WHERE `uuid` = ?");
                preparedStatement.setString(1, key.toString());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    byte[] bytes = resultSet.getBytes("value");
                    if (bytes.length != 0) {
                        try (InputStreamReader inputStreamReader = new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8)) {
                            Configuration configuration = new Configuration(inputStreamReader);
                            task.complete(configuration.getValue("player", TypeTokenAdaptor.getOFFLINE_PLAYER_TYPE()));
                        } catch (final IOException ex) {
                            MySQLDatabaseManager.handleError(ex);
                            task.complete(null);
                        }
                    } else {
                        task.complete(null);
                    }
                } else {
                    task.complete(null);
                }
                preparedStatement.close();
                resultSet.close();
            } catch (final SQLException ex) {
                MySQLDatabaseManager.handleError(ex);
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
                MySQLDatabaseManager.handleError(cause);
            }
        }).execute(TimeUnit.SECONDS, 5);
    }

    @Override
    public Task<UUID> getIDAsync(String identifier) {
        Task<UUID> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            try {
                PreparedStatement preparedStatement = this.mySQLDatabaseManager.connection.get()
                    .prepareStatement("SELECT `uuid` FROM `" + this.TABLE_NAME + "` WHERE `name` = ?");
                preparedStatement.setString(1, identifier);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    try {
                        task.complete(UUID.fromString(resultSet.getString("uuid")));
                    } catch (final Throwable throwable) {
                        MySQLDatabaseManager.handleError(throwable);
                        task.complete(null);
                    }
                } else {
                    task.complete(null);
                }
                preparedStatement.close();
                resultSet.close();
            } catch (final SQLException ex) {
                MySQLDatabaseManager.handleError(ex);
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
            try {
                PreparedStatement preparedStatement = this.mySQLDatabaseManager.connection.get()
                    .prepareStatement("SELECT `value` FROM `" + TABLE_NAME + "`");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    byte[] bytes = resultSet.getBytes("value");
                    if (bytes.length != 0) {
                        try (InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8)) {
                            Configuration configuration = new Configuration(reader);
                            consumer.accept(configuration.getValue("player", TypeTokenAdaptor.getOFFLINE_PLAYER_TYPE()));
                        } catch (final IOException ex) {
                            MySQLDatabaseManager.handleError(ex);
                        }
                    }
                }

                preparedStatement.close();
                resultSet.close();
            } catch (final SQLException ex) {
                MySQLDatabaseManager.handleError(ex);
            }

            task.complete(null);
        });
        return task;
    }

    private byte[] convert(OfflinePlayer offlinePlayer) {
        return new Configuration().addValue("player", offlinePlayer).getJsonString().getBytes(StandardCharsets.UTF_8);
    }
}
