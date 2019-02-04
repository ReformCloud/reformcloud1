/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.versioneering.VersionController;
import systems.reformcloud.versioneering.VersionUpdater;

/**
 * @author _Klaro | Pasqual K. / created on 08.01.2019
 */

public final class CommandUpdate implements Command {
    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            if (VersionController.isVersionAvailable()) {
                try {
                    commandSender.sendMessage("Trying to update the full cloud system....");
                    commandSender.sendMessage("! This will stop ReformCloud !");
                    new VersionUpdater().update();
                    commandSender.sendMessage("Update done");
                } catch (final Throwable throwable) {
                    StringUtil.printError(ReformCloudClient.getInstance().getLoggerProvider(), "An error occurred while updating CloudSystem", throwable);
                }
            } else commandSender.sendMessage("Your version is already up-to-date.");
        } else {
            commandSender.sendMessage("Checking for updates...");
            ReformCloudClient.getInstance().checkForUpdates();
        }
    }

    @Override
    public String getPermission() {
        return "reformcloud.command.update";
    }
}
