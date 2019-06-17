/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database.statistics;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.server.stats.TempServerStats;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author _Klaro | Pasqual K. / created on 03.02.2019
 */

public final class StatisticsProvider extends SaveStatisticsProvider implements Serializable {

    private static final long serialVersionUID = -209566855414655806L;

    private Stats stats;

    private final Lock writeLock = new ReentrantLock();

    @Override
    public String getName() {
        return "Statistics DB";
    }

    @Override
    public void load() {
        if (!Files.exists(Paths.get("reformcloud/database/stats/stats.json"))) {
            writeLock.lock();

            try {
                initDefaultStats();
            } finally {
                writeLock.unlock();
            }
        }

        this.stats = Configuration.parse(Paths.get("reformcloud/database/stats/stats.json"))
            .getValue("stats", new TypeToken<Stats>() {
            }.getType());
    }

    @Override
    public void save() {
        if (notAvailable()) {
            return;
        }

        writeLock.lock();

        try {
            save0();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void addLogin() {
        if (notAvailable()) {
            return;
        }

        this.stats.setLogin(this.stats.getLogin() + 1);
    }

    @Override
    public void addStartup() {
        if (notAvailable()) {
            return;
        }

        this.stats.setStartup(this.stats.getStartup() + 1);
    }

    @Override
    public void addRootStartup() {
        if (notAvailable()) {
            return;
        }

        this.stats.setRootStartup(this.stats.getRootStartup() + 1);
    }

    @Override
    public void addConsoleCommand() {
        if (notAvailable()) {
            return;
        }

        this.stats.setConsoleCommands(this.stats.getConsoleCommands() + 1);
    }

    @Override
    public void addIngameCommand() {
        if (notAvailable()) {
            return;
        }

        this.stats.setIngameCommands(this.stats.getIngameCommands() + 1);
    }

    @Override
    public void setLastStartup() {
        if (notAvailable()) {
            return;
        }

        this.stats.setLastStartup(System.currentTimeMillis());
    }

    @Override
    public void updateServerStats(final TempServerStats tempServerStats) {
        if (notAvailable()) {
            return;
        }

        this.stats.setBlocksPlaced(this.stats.getBlocksPlaced() + tempServerStats.blocksPlaced);
        this.stats
            .setWalkedDistance(this.stats.getWalkedDistance() + tempServerStats.distanceWalked);
        if (tempServerStats.onlineTime != 0) {
            this.stats
                .setServerOnlineTime(this.stats.getServerOnlineTime() + tempServerStats.onlineTime);
        }
    }

    @Override
    public void setLastShutdown() {
        if (notAvailable()) {
            return;
        }

        this.stats.setLastShutdown(System.currentTimeMillis());
    }

    @Override
    public Stats getStats() {
        return this.stats;
    }

    private void save0() {
        this.setLastShutdown();
        Configuration.parse(Paths.get("reformcloud/database/stats/stats.json"))
            .addValue("stats", this.stats)
            .write(Paths.get("reformcloud/database/stats/stats.json"));
    }

    private void initDefaultStats() {
        new Configuration().addValue("stats", new Stats(
            0,
            0,
            0,
            0,
            0,
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            0,
            0,
            0,
            0
        )).write(Paths.get("reformcloud/database/stats/stats.json"));
    }

    private boolean notAvailable() {
        return this.stats == null;
    }
}
