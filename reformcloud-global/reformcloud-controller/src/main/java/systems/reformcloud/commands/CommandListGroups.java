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
                    "    - " + e.getName() + " | Ram: " + e.getMemory() + " | ProxyModeType: " + e
                        .getProxyModeType() + " | MinOnline: " + e.getMinOnline() + " | MaxOnline: "
                        + e.getMaxOnline() + " | MaxPlayers: " + e.getMaxPlayers() + " | Version: "
                        + e.getProxyVersions().getName() + " | Clients: " + e.getClients())
            );
        ReformCloudController.getInstance().getColouredConsoleProvider().emptyLine();
        commandSender.sendMessage(
            ReformCloudController.getInstance().getLoadedLanguage().getCommand_listgroup_list()
                .replace("%type%", "server"));
        ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups().values()
            .forEach(e ->
                commandSender.sendMessage(
                    "    - " + e.getName() + " | Ram: " + e.getMemory() + " | ServerModeType: " + e
                        .getServerModeType() + " | MinOnline: " + e.getMinOnline()
                        + " | MaxOnline: " + e.getMaxOnline() + " | MaxPlayers: " + e
                        .getMaxPlayers() + " | Version: " + e.getSpigotVersions().getName()
                        + " | Clients: " + e.getClients())
            );
    }
}
