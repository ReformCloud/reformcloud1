/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.database.statistics.Stats;
import systems.reformcloud.utility.StringUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public final class CommandInfo extends Command implements Serializable {

    public CommandInfo() {
        super("info", "Prints the Cloud info", null,
            new String[]{"whoami", "whoiam", "me", "about"});
    }

    private final SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        final Stats stats = ReformCloudController.getInstance()
            .getStatisticsProvider().getStats();

        commandSender.sendMessage("ReformCloud version " + StringUtil.REFORM_VERSION);
        commandSender.sendMessage("Main developer: _Klaro");
        commandSender.sendMessage("All startup: " + stats.getStartup());
        commandSender.sendMessage("Root startup: " + stats.getRootStartup());
        commandSender.sendMessage("First startup: " + stats.getFirstStartup());
        commandSender.sendMessage("Last startup: " + stats.getLastStartup());
        commandSender.sendMessage(
            "Last shutdown: " + (stats.hasShutdown() ? stats.getLastShutdown() : "never"));
        commandSender.sendMessage("Player login: " + stats.getLogin());
        commandSender.sendMessage("Executed console command: " + stats.getConsoleCommands());
        commandSender.sendMessage("Executed ingame command: " + stats.getIngameCommands());
        commandSender.sendMessage("JVM start time: " +
            ReformCloudController.getInstance().getLoggerProvider().getDateFormat()
                .format(ReformCloudLibraryService.systemStartTime()));
        commandSender.sendMessage("JVM uptime: " +
            dataFormat.format(ReformCloudLibraryService.systemUpTime()));
        commandSender.sendMessage("GameServer online time: " +
            (stats.getServerOnlineTime() == 0 ? "00:00:00.000"
                : this.dataFormat.format(stats.getServerOnlineTime())));
        commandSender.sendMessage("GameServer walked distance: " + stats.getWalkedDistance());
        commandSender.sendMessage("GameServer placed blocks: " + stats.getBlocksPlaced());
        commandSender.sendMessage(
            "For further information please contact us on our Discord (\"https://discord.gg/uskXdVZ\")");
    }
}
