/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 06.02.2019
 */

public final class CommandListGroups extends Command implements Serializable {

    public CommandListGroups() {
        super("listgroups", "List all groups", "reformcloud.commands.listgroups",
            new String[]{"lg"});
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        commandSender.sendMessage(
            ReformCloudController.getInstance().getLoadedLanguage().getCommand_listgroup_list()
                .replace("%type%", "proxy"));
        ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().values()
            .forEach(e ->
                commandSender.sendMessage(
                    "    - §e" + e.getName() + "§r | Ram: §e" + e.getMemory() + "§r | ProxyModeType: §e" +
                        e.getProxyModeType() + "§r | MinOnline: §e" + e.getMinOnline() + "§r | MaxOnline: §e"
                        + e.getMaxOnline() + "§r | MaxPlayers: §e" + e.getMaxPlayers() + "§r | Version: §e"
                        + e.getProxyVersions().getName() + "§r | Clients: §e" + e.getClients())
            );
        ReformCloudController.getInstance().getLoggerProvider().emptyLine();
        commandSender.sendMessage(
            ReformCloudController.getInstance().getLoadedLanguage().getCommand_listgroup_list()
                .replace("%type%", "server"));
        ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups().values()
            .forEach(e ->
                commandSender.sendMessage(
                    "    - §e" + e.getName() + "§r | Ram: §e" + e.getMemory() + "§r | ServerModeType: §e" +
                        e.getServerModeType() + "§r | MinOnline: §e" + e.getMinOnline()
                        + "§r | MaxOnline: §e" + e.getMaxOnline() + "§r | MaxPlayers: §e" +
                        e.getMaxPlayers() + "§r | Version: §e" + e.getSpigotVersions().getName()
                        + "§r | Clients: §e" + e.getClients())
            );
    }
}
