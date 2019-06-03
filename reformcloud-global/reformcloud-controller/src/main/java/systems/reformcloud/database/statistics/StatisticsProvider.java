/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database.statistics;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.database.DatabaseProvider;
import systems.reformcloud.meta.server.stats.TempServerStats;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author _Klaro | Pasqual K. / created on 03.02.2019
 */

public final class StatisticsProvider extends DatabaseProvider implements Serializable {

    private static final long serialVersionUID = -209566855414655806L;

    private Stats stats;

    @Override
    public String getName() {
        return "Statistics DB";
    }

    @Override
    public void load() {
        if (!Files.exists(Paths.get("reformcloud/database/stats/stats.json"))) {
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

        this.stats = Configuration.parse(Paths.get("reformcloud/database/stats/stats.json"))
            .getValue("stats", new TypeToken<Stats>() {
            }.getType());
    }

    @Override
    public void save() {
        if (!checkAvailable()) {
            return;
        }

        this.setLastShutdown();
        Configuration.parse(Paths.get("reformcloud/database/stats/stats.json"))
            .addValue("stats", this.stats)
            .write(Paths.get("reformcloud/database/stats/stats.json"));
    }

    public void addLogin() {
        if (!checkAvailable()) {
            return;
        }

        this.stats.setLogin(this.stats.login + 1);
    }

    public void addStartup() {
        if (!checkAvailable()) {
            return;
        }

        this.stats.setStartup(this.stats.startup + 1);
    }

    public void addRootStartup() {
        if (!checkAvailable()) {
            return;
        }

        this.stats.setRootStartup(this.stats.rootStartup + 1);
    }

    public void addConsoleCommand() {
        if (!checkAvailable()) {
            return;
        }

        this.stats.setConsoleCommands(this.stats.getConsoleCommands() + 1);
    }

    public void addIngameCommand() {
        if (!checkAvailable()) {
            return;
        }

        this.stats.setIngameCommands(this.stats.getIngameCommands() + 1);
    }

    public void setLastStartup() {
        if (!checkAvailable()) {
            return;
        }

        this.stats.setLastStartup(System.currentTimeMillis());
    }

    public void updateServerStats(final TempServerStats tempServerStats) {
        if (!checkAvailable()) {
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

    private void setLastShutdown() {
        if (!checkAvailable()) {
            return;
        }

        this.stats.setLastShutdown(System.currentTimeMillis());
    }

    private boolean checkAvailable() {
        return this.stats != null;
    }

    public Stats getStats() {
        return this.stats;
    }

    public final class Stats {

        private int startup, rootStartup, login, consoleCommands, ingameCommands;
        private long firstStartup, lastStartup, lastShutdown, serverOnlineTime, walkedDistance, blocksPlaced;

        @java.beans.ConstructorProperties({"startup", "rootStartup", "login", "consoleCommands",
            "ingameCommands", "firstStartup", "lastStartup", "lastShutdown", "serverOnlineTime",
            "walkedDistance", "blocksPlaced"})
        Stats(int startup, int rootStartup, int login, int consoleCommands, int ingameCommands,
            long firstStartup, long lastStartup, long lastShutdown, long serverOnlineTime,
            long walkedDistance, long blocksPlaced) {
            this.startup = startup;
            this.rootStartup = rootStartup;
            this.login = login;
            this.consoleCommands = consoleCommands;
            this.ingameCommands = ingameCommands;
            this.firstStartup = firstStartup;
            this.lastStartup = lastStartup;
            this.lastShutdown = lastShutdown;
            this.serverOnlineTime = serverOnlineTime;
            this.walkedDistance = walkedDistance;
            this.blocksPlaced = blocksPlaced;
        }

        public String getFirstStartup() {
            return getDateFormatted(firstStartup);
        }

        public String getLastStartup() {
            return getDateFormatted(lastStartup);
        }

        public String getLastShutdown() {
            return getDateFormatted(lastShutdown);
        }

        private String getDateFormatted(final long time) {
            return ReformCloudController.getInstance().getLoggerProvider().getDateFormat()
                .format(time);
        }

        public boolean hasShutdown() {
            return this.lastShutdown != 0;
        }

        public int getStartup() {
            return this.startup;
        }

        public int getRootStartup() {
            return this.rootStartup;
        }

        public int getLogin() {
            return this.login;
        }

        public int getConsoleCommands() {
            return this.consoleCommands;
        }

        public int getIngameCommands() {
            return this.ingameCommands;
        }

        public long getServerOnlineTime() {
            return this.serverOnlineTime;
        }

        public long getWalkedDistance() {
            return this.walkedDistance;
        }

        public long getBlocksPlaced() {
            return this.blocksPlaced;
        }

        void setStartup(int startup) {
            this.startup = startup;
        }

        void setRootStartup(int rootStartup) {
            this.rootStartup = rootStartup;
        }

        void setLogin(int login) {
            this.login = login;
        }

        void setConsoleCommands(int consoleCommands) {
            this.consoleCommands = consoleCommands;
        }

        void setIngameCommands(int ingameCommands) {
            this.ingameCommands = ingameCommands;
        }

        public void setFirstStartup(long firstStartup) {
            this.firstStartup = firstStartup;
        }

        void setLastStartup(long lastStartup) {
            this.lastStartup = lastStartup;
        }

        void setLastShutdown(long lastShutdown) {
            this.lastShutdown = lastShutdown;
        }

        void setServerOnlineTime(long serverOnlineTime) {
            this.serverOnlineTime = serverOnlineTime;
        }

        void setWalkedDistance(long walkedDistance) {
            this.walkedDistance = walkedDistance;
        }

        void setBlocksPlaced(long blocksPlaced) {
            this.blocksPlaced = blocksPlaced;
        }
    }
}
