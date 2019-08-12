/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database.player.file;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.database.DataBaseManager;
import systems.reformcloud.database.config.DatabaseConfig;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.future.DefaultTask;
import systems.reformcloud.utility.future.Task;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 11.08.2019
 */

public final class FileDatabaseManager extends DataBaseManager implements Serializable {

    @Override
    public Boolean isConnected() {
        return isConnectedAsync().execute();
    }

    @Override
    public Task<Boolean> isConnectedAsync() {
        Task<Boolean> task = new DefaultTask<>();
        task.complete(true);
        return task;
    }

    @Override
    public Boolean connect(DatabaseConfig databaseConfig) {
        return connectAsync(null).execute();
    }

    @Override
    public Task<Boolean> connectAsync(DatabaseConfig databaseConfig) {
        Task<Boolean> task = new DefaultTask<>();
        task.complete(true);
        return task;
    }

    @Override
    public Void disconnect() {
        return disconnectAsync().execute();
    }

    @Override
    public Task<Void> disconnectAsync() {
        Task<Void> task = new DefaultTask<>();
        task.complete(null);
        return task;
    }

    @Override
    public DatabaseConfig getCurrentConnectedConfig() {
        throw new UnsupportedOperationException("Not supported in file database");
    }

    @Override
    public Task<DatabaseConfig> getCurrentConnectedConfigAsync() {
        throw new UnsupportedOperationException("Not supported in file database");
    }

    @Override
    public String databaseName() {
        return "LocalFile";
    }

    static void handleError(Throwable cause) {
        StringUtil.printError(
            ReformCloudController.getInstance().getColouredConsoleProvider(),
            "Error in database",
            cause
        );
    }
}
