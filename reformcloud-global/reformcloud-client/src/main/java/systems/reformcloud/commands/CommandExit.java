/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;

/**
 * @author _Klaro | Pasqual K. / created on 30.10.2018
 */

public final class CommandExit implements Command {
    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        commandSender.sendMessage("ReformCloud will stop...");
        System.exit(1);

    }

    @Override
    public String getPermission() {
        return "reformcloud.command.exit";
    }
}
