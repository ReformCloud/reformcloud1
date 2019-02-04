/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database.statistics;

import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.database.DatabaseProvider;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author _Klaro | Pasqual K. / created on 03.02.2019
 */

public final class StatisticsProvider extends DatabaseProvider implements Serializable {
    private static final long serialVersionUID = -209566855414655806L;

    @Getter
    private Stats stats;

    @Override
    public String getName() {
        return "Statistics DB";
    }

    @Override
    public void load() {
        if (!Files.exists(Paths.get("reformcloud/database/stats/Stats.json"))) {
            new Configuration().addProperty("stats", new Stats(
                    0,
                    0,
                    0,
                    0,
                    0,
                    System.currentTimeMillis(),
                    System.currentTimeMillis(),
                    0
            )).saveAsConfigurationFile(Paths.get("reformcloud/database/stats/Stats.json"));
        }

        this.stats = Configuration.loadConfiguration(Paths.get("reformcloud/database/stats/Stats.json"))
                .getValue("stats", new TypeToken<Stats>() {
                }.getType());
    }

    @Override
    public void save() {
        if (!checkAvailable())
            return;

        this.setLastShutdown();
        Configuration.loadConfiguration(Paths.get("reformcloud/database/stats/Stats.json"))
                .addProperty("stats", this.stats)
                .saveAsConfigurationFile(Paths.get("reformcloud/database/stats/Stats.json"));
    }

    public void addLogin() {
        if (!checkAvailable())
            return;

        this.stats.setLogin(this.stats.login + 1);
    }

    public void addStartup() {
        if (!checkAvailable())
            return;

        this.stats.setStartup(this.stats.startup + 1);
    }

    public void addRootStartup() {
        if (!checkAvailable())
            return;

        this.stats.setRootStartup(this.stats.rootStartup + 1);
    }

    public void addConsoleCommand() {
        if (!checkAvailable())
            return;

        this.stats.setConsoleCommands(this.stats.getConsoleCommands() + 1);
    }

    public void addIngameCommand() {
        if (!checkAvailable())
            return;

        this.stats.setIngameCommands(this.stats.getIngameCommands() + 1);
    }

    public void setLastStartup() {
        if (!checkAvailable())
            return;

        this.stats.setLastStartup(System.currentTimeMillis());
    }

    public void setLastShutdown() {
        if (!checkAvailable())
            return;

        this.stats.setLastShutdown(System.currentTimeMillis());
    }

    private boolean checkAvailable() {
        return this.stats != null;
    }

    @AllArgsConstructor
    @Data
    public final class Stats {
        private int startup, rootStartup, login, consoleCommands, ingameCommands;
        private long firstStartup, lastStartup, lastShutdown;

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
            return ReformCloudController.getInstance().getLoggerProvider().getDateFormat().format(time);
        }

        public boolean hasShutdown() {
            return this.lastShutdown != 0;
        }
    }
}
