/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 06.02.2019
 */

public final class CommandReload extends Command implements Serializable {

    public CommandReload() {
        super("reload", "Reloads the CloudSystem", "reformcloud.command.reload",
            new String[]{"rl"});
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        ReformCloudClient.getInstance().reloadAll();
    }
}
