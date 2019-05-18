/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;

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
        commandSender.sendMessage("Checking for updates...");
        ReformCloudController.getInstance().checkForUpdates();
    }
}
