/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.versioneering.VersionController;
import systems.reformcloud.versioneering.VersionUpdater;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 08.01.2019
 */

public final class CommandUpdate extends Command implements Serializable {
    public CommandUpdate() {
        super("update", "Updates the CloudSystem", "reformcloud.command.update", new String[]{"upgrade"});
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("confirm")) {
            if (VersionController.isVersionAvailable()) {
                try {
                    commandSender.sendMessage("Trying to update the full cloud system....");
                    commandSender.sendMessage("! This will stop ReformCloud !");
                    ReformCloudLibraryService.sleep(2000);
                    new VersionUpdater().update();
                    commandSender.sendMessage("Update done");
                } catch (final Throwable throwable) {
                    StringUtil.printError(ReformCloudController.getInstance().getLoggerProvider(), "An error occurred while updating CloudSystem", throwable);
                }
            } else commandSender.sendMessage("Your version is already up-to-date.");
        } else {
            commandSender.sendMessage("Checking for updates...");
            ReformCloudController.getInstance().checkForUpdates();
        }
    }
}
