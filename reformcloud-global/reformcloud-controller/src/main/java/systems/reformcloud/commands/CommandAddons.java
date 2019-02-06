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
                        .stream()
                        .forEach(e -> commandSender.sendMessage("    - " + e.getAddonName() + " | Version: " + e.getAddonClassConfig().getVersion()));
            }

            return;
        }

        if (args.length != 2) {
            commandSender.sendMessage("addons list");
            commandSender.sendMessage("addons <enable/disable> <name>");
            return;
        }

        switch (args[0]) {
            case "enable": {
                if (ReformCloudController.getInstance().getAddonParallelLoader().isAddonEnabled(args[1])) {
                    commandSender.sendMessage("Addon is already enabled");
                    return;
                }

                if (ReformCloudController.getInstance().getAddonParallelLoader().enableAddon(args[1])) {
                    commandSender.sendMessage("The Addon was loaded successfully");
                    return;
                }
                break;
            }
            case "disable": {
                if (!ReformCloudController.getInstance().getAddonParallelLoader().isAddonEnabled(args[1])) {
                    commandSender.sendMessage("Addon is not enabled");
                    return;
                }

                if (ReformCloudController.getInstance().getAddonParallelLoader().disableAddon(args[1])) {
                    commandSender.sendMessage("The Addon was disabled successfully");
                    return;
                }
                break;
            }
            default: {
                commandSender.sendMessage("addons list");
                commandSender.sendMessage("addons <enable/disable> <name>");
                break;
            }
        }
    }
}
