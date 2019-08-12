/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database.player.mongo;

import com.google.gson.JsonElement;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.database.Database;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.utility.Require;
import systems.reformcloud.utility.TypeTokenAdaptor;
import systems.reformcloud.utility.future.DefaultTask;
import systems.reformcloud.utility.future.Task;
import systems.reformcloud.utility.future.TaskListener;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * @author _Klaro | Pasqual K. / created on 11.08.2019
 */

public final class PlayerMongoDatabase extends Database<UUID, String, OfflinePlayer> implements Serializable {

    private static final String TABLE_NAME = "players";

    public PlayerMongoDatabase(MongoDatabase mongoDatabase) {
        this.documents = mongoDatabase.getCollection(TABLE_NAME);
    }

    private final MongoCollection<Document> documents;

    @Override
    public OfflinePlayer insert(UUID key, OfflinePlayer value) {
        return insertAsync(key, value).addListener(new TaskListener<OfflinePlayer>() {
            @Override
            public void onTimeOut() {
                onOperationFailed(new TimeoutException());
            }

            @Override
            public void onOperationFailed(Throwable cause) {
                MongoDatabaseManager.handleError(cause);
            }
        }).execute(TimeUnit.SECONDS, 5);
    }

    @Override
    public Task<OfflinePlayer> insertAsync(UUID key, OfflinePlayer value) {
        Task<OfflinePlayer> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            Document document = documents.find(Filters.eq("uuid", key)).first();
            if (document != null) {
                task.complete(convert(document.toJson()));
            } else {
                documents.insertOne(ReformCloudLibraryService.GSON.fromJson(convert(value), Document.class)
                    .append("name", value.getName()).append("uuid", key));
                task.complete(value);
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
                MongoDatabaseManager.handleError(cause);
            }
        }).execute(TimeUnit.SECONDS, 5);
    }

    @Override
    public Task<Boolean> removeAsync(UUID key) {
        Task<Boolean> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            documents.deleteOne(Filters.eq("uuid", key));
            task.complete(true);
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
                MongoDatabaseManager.handleError(cause);
            }
        }).execute(TimeUnit.SECONDS, 5);
    }

    @Override
    public Task<Boolean> updateAsync(UUID key, OfflinePlayer newValue) {
        Task<Boolean> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            Document document = documents.find(Filters.eq("uuid", key)).first();
            if (document != null) {
                documents.updateOne(
                    document,
                    ReformCloudLibraryService.GSON.fromJson(convert(newValue), Document.class)
                        .append("name", newValue.getName()).append("uuid", key)
                );
                task.complete(true);
            } else {
                task.complete(false);
            }
        });
        return task;
    }

    @Override
    public Boolean contains(UUID key) {
        return containsAsync(key).addListener(new TaskListener<Boolean>() {
            @Override
            public void onTimeOut() {
                onOperationFailed(new TimeoutException());
            }

            @Override
            public void onOperationFailed(Throwable cause) {
                MongoDatabaseManager.handleError(cause);
            }
        }).execute(TimeUnit.SECONDS, 5);
    }

    @Override
    public Task<Boolean> containsAsync(UUID key) {
        Task<Boolean> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> task.complete(documents.find(Filters.eq("uuid", key)).first() != null));
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
                MongoDatabaseManager.handleError(cause);
            }
        }).execute(TimeUnit.SECONDS, 5);
    }

    @Override
    public Task<OfflinePlayer> getAsync(UUID key) {
        Task<OfflinePlayer> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            Document document = documents.find(Filters.eq("uuid", key)).first();
            if (document != null) {
                task.complete(convert(document.toJson()));
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
                MongoDatabaseManager.handleError(cause);
            }
        }).execute(TimeUnit.SECONDS, 5);
    }

    @Override
    public Task<UUID> getIDAsync(String identifier) {
        Task<UUID> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            Document document = documents.find(Filters.eq("name", identifier)).first();
            if (document != null) {
                Configuration configuration = convertToConfig(document.toJson());
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
            documents.find().forEach((Consumer<? super Document>) document -> {
                if (document != null) {
                    consumer.accept(convert(document.toJson()));
                }
            });
            task.complete(null);
        });
        return task;
    }

    private String convert(OfflinePlayer offlinePlayer) {
        Configuration configuration = new Configuration();
        configuration.addValue("player", offlinePlayer);
        return configuration.getJsonString();
    }

    private OfflinePlayer convert(String json) {
        return convertToConfig(json).getValue("player", TypeTokenAdaptor.getOFFLINE_PLAYER_TYPE());
    }

    private Configuration convertToConfig(String json) {
        JsonElement jsonElement = null;
        try {
            jsonElement = ReformCloudLibraryService.PARSER.parse(json);
        } catch (final Throwable throwable) {
            MongoDatabaseManager.handleError(throwable);
        }

        Require.requireNotNull(jsonElement);
        Require.isTrue(jsonElement.isJsonObject(), "Json Element has to be a json object");
        return new Configuration(jsonElement.getAsJsonObject());
    }
}
