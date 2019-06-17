/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 03.02.2019
 */

public final class CommandAddons extends Command implements Serializable {

    public CommandAddons() {
        super("addons", "List, enable and disable addons", "reformcloud.command.addons",
            new String[0]);
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            if (ReformCloudClient.getInstance().getAddonParallelLoader().getJavaAddons().size()
                == 0) {
                commandSender.sendMessage("There are no addons loaded");
            } else {
                commandSender.sendMessage("The following addons are loaded: ");
                ReformCloudClient.getInstance().getColouredConsoleProvider().emptyLine();
                ReformCloudClient.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .forEach(e -> commandSender.sendMessage(
                        "    - " + e.getAddonName() + " | Version: " + e.getAddonClassConfig()
                            .getVersion()));
                ReformCloudClient.getInstance().getColouredConsoleProvider().emptyLine();
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
                if (ReformCloudClient.getInstance().getAddonParallelLoader()
                    .isAddonEnabled(args[1])) {
                    commandSender.sendMessage("Addon is already enabled");
                    return;
                }

                if (ReformCloudClient.getInstance().getAddonParallelLoader().enableAddon(args[1])) {
                    commandSender.sendMessage("The Addon was loaded successfully");
                    return;
                }
                break;
            }
            case "disable": {
                if (!ReformCloudClient.getInstance().getAddonParallelLoader()
                    .isAddonEnabled(args[1])) {
                    commandSender.sendMessage("Addon is not enabled");
                    return;
                }

                if (ReformCloudClient.getInstance().getAddonParallelLoader()
                    .disableAddon(args[1])) {
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
