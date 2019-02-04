/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.examples.command;

import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

public class CommandExample implements Command {
    /**
     * This method get called when a the specific command get executed
     *
     * Dont forget to register the Command via ReformCloudController instance
     */
    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        commandSender.sendMessage("Nope");
    }

    /**
     * Sets the command permission for your own {@link CommandSender}
     * If the permission is null no permission is needed
     */
    @Override
    public String getPermission() {
        return null;
    }
}
