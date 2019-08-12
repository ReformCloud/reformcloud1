/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database.config;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public final class DatabaseConfig implements Serializable {

    public DatabaseConfig(String host, int port, String userName, String password, String database, DataBaseType dataBaseType) {
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.database = database;
        this.dataBaseType = dataBaseType;
    }

    private String host;

    private int port;

    private String userName;

    private String password;

    private String database;

    private DataBaseType dataBaseType;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase() {
        return database;
    }

    public DataBaseType getDataBaseType() {
        return dataBaseType;
    }

    public enum DataBaseType {
        MYSQL,

        MONGODB,

        FILE;

        public static DataBaseType find(String in) {
            try {
                return valueOf(in.toUpperCase());
            } catch (final Throwable throwable) {
                return null;
            }
        }
    }
}
