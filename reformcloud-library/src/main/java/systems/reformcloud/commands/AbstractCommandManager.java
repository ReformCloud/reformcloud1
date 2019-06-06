/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 15.04.2019
 */

public abstract class AbstractCommandManager implements Serializable {

    /**
     * Dispatches a command
     *
     * @param commandSender The command sender who sent the command
     * @param command The command which was executed
     * @return If the execution was successful
     */
    public abstract boolean dispatchCommand(CommandSender commandSender, String command);

    /**
     * Registers a command
     *
     * @param command The command which should be registered
     * @return The current instance of this class
     */
    public abstract AbstractCommandManager registerCommand(Command command);

    /**
     * Clears all commands
     */
    public abstract void clearCommands();

    /**
     * Unregisters a specific command
     *
     * @param command The command which should be unregistered
     */
    public abstract void unregisterCommand(String command);

    /**
     * Get if a specific command is registered
     *
     * @param command The command which should be checked
     * @return If the command is registered
     */
    public abstract boolean isCommandRegistered(String command);

    /**
     * Gets all registered commands
     *
     * @return A list containing all registered commands
     */
    public abstract List<String> getCommandsAsString();

    /**
     * Dispatches a command using the default command manager
     *
     * @param command The command which should be dispatched
     * @return If the execution was successful
     */
    public abstract boolean dispatchCommand(String command);

    /**
     * Creates a new command manger
     *
     * @return A new instance of this class
     */
    public AbstractCommandManager defaultCommandManager() {
        return new CommandManager();
    }
}
