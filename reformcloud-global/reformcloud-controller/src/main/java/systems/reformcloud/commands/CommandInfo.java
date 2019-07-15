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
import java.util.LinkedList;
import java.util.List;

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

        final List<Integer> openedPorts = new LinkedList<>();

        ReformCloudController.getInstance().getAllRegisteredServers().
            forEach(e -> openedPorts.add(e.getPort()));
        ReformCloudController.getInstance().getAllRegisteredProxies().
            forEach(e -> openedPorts.add(e.getPort()));

        commandSender.sendMessage("ReformCloud version §e" + StringUtil.REFORM_VERSION);
        commandSender.sendMessage("Main developer: §e_Klaro");
        commandSender.sendMessage("All startup: §e" + stats.getStartup());
        commandSender.sendMessage("Root startup: §e" + stats.getRootStartup());
        commandSender.sendMessage("First startup: §e" + stats.getFirstStartup());
        commandSender.sendMessage("Last startup: §e" + stats.getLastStartup());
        commandSender.sendMessage(
            "Last shutdown: §e" + (stats.hasShutdown() ? stats.getLastShutdown() : "never"));
        commandSender.sendMessage("Player login: §e" + stats.getLogin());
        commandSender.sendMessage("Executed console command: §e" + stats.getConsoleCommands());
        commandSender.sendMessage("Executed ingame command: §e" + stats.getIngameCommands());
        commandSender.sendMessage("JVM start time: §e" +
            ReformCloudController.getInstance().getColouredConsoleProvider().getDateFormat()
                .format(ReformCloudLibraryService.systemStartTime()));
        commandSender.sendMessage("JVM uptime: §e" +
            dataFormat.format(ReformCloudLibraryService.systemUpTime()));
        commandSender.sendMessage("GameServer online time: §e" +
            (stats.getServerOnlineTime() == 0 ? "00:00:00.000"
                : this.dataFormat.format(stats.getServerOnlineTime())));
        commandSender.sendMessage("GameServer walked distance: §e" + stats.getWalkedDistance());
        commandSender.sendMessage("GameServer placed blocks: §e" + stats.getBlocksPlaced());
        commandSender.sendMessage("Opened Ports: §e" + (openedPorts.size() > 0 ? openedPorts
            .toString()
            .replace("[", "")
            .replace("]", "") : "no opened ports"));
        commandSender.sendMessage(
            "§bFor further information please contact us on our Discord (\"https://discord.gg/uskXdVZ\")");
    }
}
