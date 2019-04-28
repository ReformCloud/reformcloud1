/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database;

import systems.reformcloud.ReformCloudController;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 28.03.2019
 */

public final class DatabaseSaver implements Serializable, Runnable {
    @Override
    public void run() {
        ReformCloudController.getInstance().getDatabaseProviders().forEach(DatabaseProvider::save);
    }
}
