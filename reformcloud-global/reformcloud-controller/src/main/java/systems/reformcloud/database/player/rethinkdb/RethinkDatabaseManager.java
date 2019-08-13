/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database.player.rethinkdb;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.addons.dependency.DependencyLoader;
import systems.reformcloud.addons.dependency.util.DynamicDependency;
import systems.reformcloud.database.DataBaseManager;
import systems.reformcloud.database.config.DatabaseConfig;
import systems.reformcloud.database.player.rethinkdb.dependency.RethinkDBJavaDriver;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.future.DefaultTask;
import systems.reformcloud.utility.future.Task;
import systems.reformcloud.utility.future.TaskListener;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

/**
 * @author _Klaro | Pasqual K. / created on 12.08.2019
 */

public final class RethinkDatabaseManager extends DataBaseManager implements Serializable {

    private static final DynamicDependency DEPENDENCY = new RethinkDBJavaDriver();

    public RethinkDatabaseManager() {
        DependencyLoader.loadDependency(DEPENDENCY);
    }

    public Connection connection;

    private DatabaseConfig databaseConfig;

    @Override
    public Boolean isConnected() {
        return isConnectedAsync().execute();
    }

    @Override
    public Task<Boolean> isConnectedAsync() {
        Task<Boolean> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> task.complete(connection != null && connection.isOpen()));
        return task;
    }

    @Override
    public Boolean connect(DatabaseConfig databaseConfig) {
        return connectAsync(databaseConfig).addListener(new TaskListener<Boolean>() {
            @Override
            public void onTimeOut() {
                onOperationFailed(new TimeoutException("Database connection request timed out"));
            }

            @Override
            public void onOperationFailed(Throwable cause) {
                handleError(cause);
            }
        }).execute();
    }

    @Override
    public Task<Boolean> connectAsync(DatabaseConfig databaseConfig) {
        Task<Boolean> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            ReformCloudController.getInstance().getColouredConsoleProvider().serve().accept("CONNECT");
            this.connection = RethinkDB.r.connection()
                .hostname(databaseConfig.getHost())
                .port(databaseConfig.getPort())
                .user(databaseConfig.getUserName(), databaseConfig.getPassword())
                .db(databaseConfig.getDatabase())
                .timeout(30)
                .connect();
            ReformCloudController.getInstance().getColouredConsoleProvider().serve().accept("DONE");
            RethinkDB.r.dbCreate(databaseConfig.getDatabase()).run(connection);
            ReformCloudController.getInstance().getColouredConsoleProvider().serve().accept("CREATED");
            connection.use(databaseConfig.getDatabase());
            ReformCloudController.getInstance().getColouredConsoleProvider().serve().accept("USED");
            task.complete(isConnected());
            ReformCloudController.getInstance().getColouredConsoleProvider().serve().accept("COMPLETED");
            this.databaseConfig = databaseConfig;
        });
        return task;
    }

    @Override
    public Void disconnect() {
        return disconnectAsync().execute();
    }

    @Override
    public Task<Void> disconnectAsync() {
        Task<Void> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            if (this.connection != null) {
                this.connection.close();
                this.connection = null;
                this.databaseConfig = null;
            }
            task.complete(null);
        });
        return task;
    }

    @Override
    public DatabaseConfig getCurrentConnectedConfig() {
        return getCurrentConnectedConfigAsync().execute();
    }

    @Override
    public Task<DatabaseConfig> getCurrentConnectedConfigAsync() {
        Task<DatabaseConfig> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            task.complete(databaseConfig);
        });
        return task;
    }

    @Override
    public String databaseName() {
        return "RethinkDB";
    }

    static void handleError(Throwable cause) {
        StringUtil.printError(
            ReformCloudController.getInstance().getColouredConsoleProvider(),
            "Error in database",
            cause
        );
    }
}
