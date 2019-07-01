/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database.statistics;

import systems.reformcloud.ReformCloudController;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 17.06.2019
 */

public final class Stats implements Serializable {

    private int startup;

    private int rootStartup;

    private int login;

    private int consoleCommands;

    private int ingameCommands;

    private long firstStartup;

    private long lastStartup;

    private long lastShutdown;

    private long serverOnlineTime;

    private long walkedDistance;

    private long blocksPlaced;

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
        return ReformCloudController.getInstance().getColouredConsoleProvider().getDateFormat()
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
