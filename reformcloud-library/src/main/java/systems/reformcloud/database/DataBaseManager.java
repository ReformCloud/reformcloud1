/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database;

import systems.reformcloud.database.config.DatabaseConfig;
import systems.reformcloud.utility.future.Task;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public abstract class DataBaseManager implements Serializable {

    public abstract Boolean isConnected();

    public abstract Task<Boolean> isConnectedAsync();

    public abstract Boolean connect(DatabaseConfig databaseConfig);

    public abstract Task<Boolean> connectAsync(DatabaseConfig databaseConfig);

    public abstract Void disconnect();

    public abstract Task<Void> disconnectAsync();

    public abstract DatabaseConfig getCurrentConnectedConfig();

    public abstract Task<DatabaseConfig> getCurrentConnectedConfigAsync();

    public abstract String databaseName();
}
