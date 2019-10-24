/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database.player.mysql;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.addons.dependency.DependencyLoader;
import systems.reformcloud.addons.dependency.util.DynamicDependency;
import systems.reformcloud.database.DataBaseManager;
import systems.reformcloud.database.config.DatabaseConfig;
import systems.reformcloud.database.player.mysql.dependency.MySQLConnectorJava;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.future.DefaultTask;
import systems.reformcloud.utility.future.Task;
import systems.reformcloud.utility.future.TaskListener;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public final class MySQLDatabaseManager extends DataBaseManager implements Serializable {

    private static final DynamicDependency DEPENDENCY = new MySQLConnectorJava();

    private static final String CONNECT_ARGUMENTS = "autoReconnect=true&useUnicode=true&useJDBCCompliantTimezoneShift=true" +
        "&useLegacyDatetimeCode=false&serverTimezone=UTC";

    final AtomicReference<Connection> connection = new AtomicReference<>();

    private final AtomicBoolean connectionStatus = new AtomicBoolean(false);

    private final AtomicReference<DatabaseConfig> config = new AtomicReference<>();

    public MySQLDatabaseManager() {
        DependencyLoader.loadDependency(DEPENDENCY);
    }

    @Override
    public Boolean isConnected() {
        return isConnectedAsync().execute();
    }

    @Override
    public Task<Boolean> isConnectedAsync() {
        DefaultTask<Boolean> defaultTask = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            try {
                Connection connection = MySQLDatabaseManager.this.connection.get();
                defaultTask.complete(connection != null && !connection.isClosed() && connection.isValid(250));
            } catch (final SQLException ex) {
                handleError(ex);
                defaultTask.complete(false);
            }
        });
        return defaultTask;
    }

    @Override
    public Boolean connect(DatabaseConfig databaseConfig) {
        return connectAsync(databaseConfig).addListener(new TaskListener<Boolean>() {
            @Override
            public void onOperationFailed(Throwable cause) {
                handleError(cause);
            }
        }).execute();
    }

    @Override
    public Task<Boolean> connectAsync(DatabaseConfig databaseConfig) {
        DefaultTask<Boolean> defaultTask = new DefaultTask<>();
        if (connectionStatus.get()) {
            defaultTask.complete(false);
            return defaultTask;
        }

        CompletableFuture.runAsync(() -> {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                config.set(databaseConfig);
                connection.set(DriverManager.getConnection(
                    MessageFormat.format(
                        "jdbc:mysql://{0}:{1}/{2}?" + CONNECT_ARGUMENTS,
                        databaseConfig.getHost(),
                        Integer.toString(databaseConfig.getPort()),
                        databaseConfig.getDatabase()
                    ), databaseConfig.getUserName(), databaseConfig.getPassword()
                ));
                if (isConnected()) {
                    connectionStatus.set(true);
                    defaultTask.complete(true);
                    Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {

                        try {
                            if (isConnected()) {
                                PreparedStatement preparedStatement = connection.get()
                                    .prepareStatement("CREATE TABLE IF NOT EXISTS `players`" +
                                        " (`uuid` VARCHAR(64), `name` VARCHAR(16), `value` LONGBLOB);");
                                preparedStatement.executeUpdate();
                                preparedStatement.close();
                            }
                        } catch (final SQLException ex) {
                            handleError(ex);
                        }

                    }, 5, 5, TimeUnit.MINUTES);
                } else {
                    defaultTask.complete(false);
                }
            } catch (final Exception ex) {
                handleError(ex);
            }
        });
        return defaultTask;
    }

    @Override
    public Void disconnect() {
        return disconnectAsync().addListener(new TaskListener<Void>() {
            @Override
            public void onTimeOut() {
                onOperationFailed(new TimeoutException());
            }

            @Override
            public void onOperationFailed(Throwable cause) {
                handleError(cause);
            }
        }).execute(TimeUnit.SECONDS, 5);
    }

    @Override
    public Task<Void> disconnectAsync() {
        DefaultTask<Void> defaultTask = new DefaultTask<>();
        if (!connectionStatus.get() || !isConnected()) {
            defaultTask.complete(null);
            return defaultTask;
        }

        CompletableFuture.runAsync(() -> {
            Connection connection = MySQLDatabaseManager.this.connection.get();
            if (connection == null) {
                defaultTask.complete(null);
            } else {
                try {
                    connection.close();
                } catch (final SQLException ex) {
                    handleError(ex);
                }

                connectionStatus.set(false);
                this.connection.set(null);
                defaultTask.complete(null);
            }
        });
        return defaultTask;
    }

    @Override
    public DatabaseConfig getCurrentConnectedConfig() {
        return getCurrentConnectedConfigAsync().addListener(new TaskListener<DatabaseConfig>() {
            @Override
            public void onOperationFailed(Throwable cause) {
                handleError(cause);
            }
        }).execute();
    }

    @Override
    public Task<DatabaseConfig> getCurrentConnectedConfigAsync() {
        Task<DatabaseConfig> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> task.complete(config.get()));
        return task;
    }

    @Override
    public String databaseName() {
        return "MySQL";
    }

    static void handleError(Throwable cause) {
        StringUtil.printError(
            ReformCloudController.getInstance().getColouredConsoleProvider(),
            "Error in database",
            cause
        );
    }
}
