/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database.player.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.addons.dependency.DependencyLoader;
import systems.reformcloud.addons.dependency.util.DynamicDependency;
import systems.reformcloud.database.DataBaseManager;
import systems.reformcloud.database.config.DatabaseConfig;
import systems.reformcloud.database.player.mongo.dependency.MongoDatabaseJavaDriver;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.future.DefaultTask;
import systems.reformcloud.utility.future.Task;
import systems.reformcloud.utility.future.TaskListener;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author _Klaro | Pasqual K. / created on 11.08.2019
 */

public final class MongoDatabaseManager extends DataBaseManager implements Serializable {

    private static final DynamicDependency DEPENDENCY = new MongoDatabaseJavaDriver();

    public MongoDatabaseManager() {
        DependencyLoader.loadDependency(DEPENDENCY);
    }

    private MongoClient mongoClient;

    public MongoDatabase mongoDatabase;

    private DatabaseConfig databaseConfig;

    @Override
    public Boolean isConnected() {
        return isConnectedAsync().execute();
    }

    @Override
    public Task<Boolean> isConnectedAsync() {
        Task<Boolean> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> task.complete(mongoClient != null));
        return task;
    }

    @Override
    public Boolean connect(DatabaseConfig databaseConfig) {
        return connectAsync(databaseConfig).addListener(new TaskListener<Boolean>() {
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
    public Task<Boolean> connectAsync(DatabaseConfig databaseConfig) {
        Task<Boolean> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            try {
                this.mongoClient = MongoClients.create(
                    new ConnectionString(
                        MessageFormat.format(
                            "mongodb://{0}:{1}@{2}:{3}/{4}",
                            databaseConfig.getUserName(),
                            URLEncoder.encode(databaseConfig.getPassword(), StandardCharsets.UTF_8.name()),
                            databaseConfig.getHost(),
                            Integer.toString(databaseConfig.getPort()),
                            databaseConfig.getDatabase()
                        )
                    )
                );
                this.mongoDatabase = mongoClient.getDatabase(databaseConfig.getDatabase());
                this.databaseConfig = databaseConfig;
                task.complete(true);
            } catch (final UnsupportedEncodingException ex) {
                handleError(ex);
                task.complete(false);
            }
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
            if (isConnected()) {
                this.mongoClient.close();
                this.mongoClient = null;
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
        CompletableFuture.runAsync(() -> task.complete(this.databaseConfig));
        return task;
    }

    @Override
    public String databaseName() {
        return "MongoDB";
    }

    static void handleError(Throwable cause) {
        StringUtil.printError(
            ReformCloudController.getInstance().getColouredConsoleProvider(),
            "Error in database",
            cause
        );
    }
}
