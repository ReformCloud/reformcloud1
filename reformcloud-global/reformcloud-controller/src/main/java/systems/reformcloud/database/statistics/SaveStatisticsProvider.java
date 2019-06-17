/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database.statistics;

import systems.reformcloud.database.DatabaseProvider;
import systems.reformcloud.meta.server.stats.TempServerStats;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 17.06.2019
 */

public abstract class SaveStatisticsProvider extends DatabaseProvider implements
    Serializable {

    public abstract void addLogin();

    public abstract void addStartup();

    public abstract void addRootStartup();

    public abstract void addConsoleCommand();

    public abstract void addIngameCommand();

    public abstract void setLastStartup();

    public abstract void updateServerStats(TempServerStats arg1);

    public abstract void setLastShutdown();

    public abstract Stats getStats();
}
