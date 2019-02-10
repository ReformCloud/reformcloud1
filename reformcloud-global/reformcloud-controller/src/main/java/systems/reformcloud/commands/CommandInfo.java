/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;
import systems.reformcloud.database.statistics.StatisticsProvider;
import systems.reformcloud.utility.StringUtil;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public final class CommandInfo extends Command implements Serializable {
    public CommandInfo() {
        super("info", "Prints the Cloud info", null, new String[0]);
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        final StatisticsProvider.Stats stats = ReformCloudController.getInstance().getStatisticsProvider().getStats();

        commandSender.sendMessage("ReformCloud version " + StringUtil.REFORM_VERSION + "@" + StringUtil.REFORM_SPECIFICATION);
        commandSender.sendMessage("Main developer: _Klaro");
        commandSender.sendMessage("All startup: " + stats.getStartup());
        commandSender.sendMessage("Root startup: " + stats.getRootStartup());
        commandSender.sendMessage("First startup: " + stats.getFirstStartup());
        commandSender.sendMessage("Last startup: " + stats.getLastStartup());
        commandSender.sendMessage("Last shutdown: " + (stats.hasShutdown() ? stats.getLastShutdown() : "never"));
        commandSender.sendMessage("Player login: " + stats.getLogin());
        commandSender.sendMessage("Executed console command: " + stats.getConsoleCommands());
        commandSender.sendMessage("Executed ingame command: " + stats.getIngameCommands());
        commandSender.sendMessage("For further information please contact us on our Discord (\"https://discord.gg/uskXdVZ\")");
    }
}
