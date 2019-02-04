/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;
import systems.reformcloud.utility.StringUtil;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public final class CommandReload implements Command {
    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        try {
            commandSender.sendMessage("Trying to reload the full cloud system....");
            ReformCloudController.getInstance().reloadAll();
        } catch (final Throwable throwable) {
            StringUtil.printError(ReformCloudController.getInstance().getLoggerProvider(), "An error occurred while reloading CloudSystem", throwable);
        }
    }

    @Override
    public String getPermission() {
        return "reformcloud.command.reload";
    }
}
