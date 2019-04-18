/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 03.02.2019
 */

public final class CommandAddons extends Command implements Serializable {
    public CommandAddons() {
        super("addons", "List, enable and disable addons", "reformcloud.command.addons", new String[0]);
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            if (ReformCloudController.getInstance().getAddonParallelLoader().getJavaAddons().size() == 0) {
                commandSender.sendMessage("There are no addons loaded");
            } else {
                commandSender.sendMessage("The following addons are loaded: ");
                ReformCloudController.getInstance().getLoggerProvider().emptyLine();
                ReformCloudController.getInstance().getAddonParallelLoader()
                        .getJavaAddons()
                        .forEach(e -> commandSender.sendMessage("    - " +
                                e.getAddonName() + " | Version: " + e.getAddonClassConfig().getVersion() +
                                " | Main-Class: " + e.getAddonClassConfig().getMain()));
            }

            return;
        } else {
            commandSender.sendMessage("addons list");
        }
    }
}
