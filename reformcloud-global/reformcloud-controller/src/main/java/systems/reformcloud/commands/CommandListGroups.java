/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 06.02.2019
 */

public final class CommandListGroups extends Command implements Serializable {
    public CommandListGroups() {
        super("listgroups", "List all groups", "reformcloud.commands.listgroups", new String[]{"lg"});
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        commandSender.sendMessage("The following ProxyGroups are registered: ");
        ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().values().forEach(e ->
                commandSender.sendMessage("    - " + e.getName() + " | Ram: " + e.getMemory() + " | MinOnline: " + e.getMinOnline() + " | MaxOnline: " + e.getMaxOnline() + " | MaxPlayers: " + e.getMaxPlayers() + " | Version: " + e.getProxyVersions().getName() + " | Clients: " + e.getClients())
        );
        ReformCloudController.getInstance().getLoggerProvider().emptyLine();
        commandSender.sendMessage("The following ServerGroups are registered: ");
        ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups().values().forEach(e ->
                commandSender.sendMessage("    - " + e.getName() + " | Ram: " + e.getMemory() + " | MinOnline: " + e.getMinOnline() + " | MaxOnline: " + e.getMaxOnline() + " | MaxPlayers: " + e.getMaxPlayers() + " | Version: " + e.getSpigotVersions().getName() + " | Clients: " + e.getClients())
        );
    }
}
