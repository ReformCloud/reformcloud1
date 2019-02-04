/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;

/**
 * @author _Klaro | Pasqual K. / created on 24.12.2018
 */

public final class CommandHelp implements Command {

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        commandSender.sendMessage("The following Commands are registered:");
        ReformCloudClient.getInstance().getCommandManager().getCommands().forEach(command -> commandSender.sendMessage("   \"" + command + "\""));
    }

    @Override
    public String getPermission() {
        return null;
    }
}
