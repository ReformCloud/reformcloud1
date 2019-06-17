/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.utility.StringUtil;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public final class CommandReload extends Command implements Serializable {

    public CommandReload() {
        super("reload", "Reloads the CloudSystem", "reformcloud.command.reload",
            new String[]{"rl"});
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        try {
            ReformCloudController.getInstance().reloadAll();
        } catch (final Throwable throwable) {
            StringUtil.printError(ReformCloudController.getInstance().getColouredConsoleProvider(),
                "An error occurred while reloading CloudSystem", throwable);
        }
    }
}
