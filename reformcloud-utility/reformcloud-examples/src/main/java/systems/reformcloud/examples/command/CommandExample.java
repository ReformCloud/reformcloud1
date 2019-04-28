/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.examples.command;

import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

public final class CommandExample extends Command implements Serializable {
    /**
     * The main constructor, must be in every command you create
     *
     * @param name        The main command name (for ex. "process")
     * @param description The description for the command, will be shown in
     *                    the help command
     * @param permission  The permission for the command. If the permission
     *                    is {@code null}, no permission is required to execute
     *                    the command
     * @param aliases     The aliases of the command (for ex. "pro") or just use
     *                    {@code new String[0]} here
     */
    public CommandExample(String name, String description, String permission, String[] aliases) {
        super(name, description, permission, aliases);
    }

    /**
     * This method get called when a the specific command get executed
     * <p>
     * Dont forget to register the Command via ReformCloudController instance
     */
    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        commandSender.sendMessage("Nope");
    }
}
