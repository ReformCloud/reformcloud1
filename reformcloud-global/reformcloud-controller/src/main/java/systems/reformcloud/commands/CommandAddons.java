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

public final class CommandAddons implements Serializable, Command {
    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (ReformCloudController.getInstance().getAddonParallelLoader().getJavaAddons().size() == 0) {
            commandSender.sendMessage("There are no addons loaded");
        } else {
            commandSender.sendMessage("The following addons are loaded: ");
            ReformCloudController.getInstance().getLoggerProvider().emptyLine();
            ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .forEach(e -> commandSender.sendMessage("    - " + e.getAddonName() + " | Version: " + e.getAddonClassConfig().getVersion()));
            ReformCloudController.getInstance().getLoggerProvider().emptyLine();
        }
    }

    @Override
    public String getPermission() {
        return null;
    }
}
