/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.backup;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 12.05.2019
 */

public final class FTPConfig implements Serializable {
    private boolean useFTPS, saveController, saveClient;
    private String host, userName, password;
    private int port;
    private int saveIntervallInMinutes;
    private List<String> excluded;

    @ConstructorProperties({"useFTPS", "saveController", "saveClient", "host", "userName", "password", "port", "saveIntervallInMinutes", "excluded"})
    public FTPConfig(boolean useFTPS, boolean saveController, boolean saveClient, String host, String userName,
                     String password, int port, int saveIntervallInMinutes, List<String> excluded) {
        this.useFTPS = useFTPS;
        this.saveController = saveController;
        this.saveClient = saveClient;
        this.host = host;
        this.userName = userName;
        this.password = password;
        this.port = port;
        this.saveIntervallInMinutes = saveIntervallInMinutes;
        this.excluded = excluded;
    }

    public boolean isUseFTPS() {
        return useFTPS;
    }

    public boolean isSaveController() {
        return saveController;
    }

    public boolean isSaveClient() {
        return saveClient;
    }

    public String getHost() {
        return host;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

    public int getSaveIntervallInMinutes() {
        return saveIntervallInMinutes;
    }

    public List<String> getExcluded() {
        return excluded;
    }
}
