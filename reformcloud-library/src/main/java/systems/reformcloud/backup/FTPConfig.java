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

    private boolean useFTPS;

    private boolean deleteLocalBackupAfterUpload;

    private boolean saveController;

    private boolean saveClient;

    private String host;

    private String userName;

    private String password;

    private int port;

    private int saveIntervalInMinutes;

    private List<String> excluded;

    @ConstructorProperties({"useFTPS", "deleteLocalBackupAfterUpload", "saveController",
        "saveClient", "host", "userName", "password", "port", "saveIntervalInMinutes", "excluded"})
    public FTPConfig(boolean useFTPS, boolean deleteLocalBackupAfterUpload, boolean saveController,
        boolean saveClient,
        String host, String userName, String password, int port, int saveIntervalInMinutes,
        List<String> excluded) {
        this.useFTPS = useFTPS;
        this.deleteLocalBackupAfterUpload = deleteLocalBackupAfterUpload;
        this.saveController = saveController;
        this.saveClient = saveClient;
        this.host = host;
        this.userName = userName;
        this.password = password;
        this.port = port;
        this.saveIntervalInMinutes = saveIntervalInMinutes;
        this.excluded = excluded;
    }

    public boolean isUseFTPS() {
        return useFTPS;
    }

    public boolean isDeleteLocalBackupAfterUpload() {
        return deleteLocalBackupAfterUpload;
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

    public int getSaveIntervalInMinutes() {
        return saveIntervalInMinutes;
    }

    public List<String> getExcluded() {
        return excluded;
    }
}
