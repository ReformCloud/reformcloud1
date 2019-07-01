/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.utility.annotiations.MayNotBePresent;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 15.04.2019
 */

public abstract class AbstractCommandManager implements Serializable {

    /**
     * Dispatches a command
     *
     * @param arg1 The command sender who sent the command
     * @param arg2 The command which was executed
     * @return If the execution was successful
     */
    public abstract boolean dispatchCommand(CommandSender arg1, String arg2);

    /**
     * Registers a command
     *
     * @param arg1 The command which should be registered
     * @return The current instance of this class
     */
    public abstract AbstractCommandManager registerCommand(Command arg1);

    /**
     * The list of all registered commands
     *
     * @return A list containing all registered commands
     */
    public abstract List<Command> commands();

    /**
     * Clears all commands
     */
    public abstract void clearCommands();

    /**
     * Finds a specific command
     *
     * @param arg1 The name of the command
     * @return The command or {@code null} if the command is not registered
     */
    @MayNotBePresent
    public abstract Command findCommand(String arg1);

    /**
     * Unregisters a specific command
     *
     * @param arg1 The command which should be unregistered
     */
    public abstract void unregisterCommand(String arg1);

    /**
     * Get if a specific command is registered
     *
     * @param arg1 The command which should be checked
     * @return If the command is registered
     */
    public abstract boolean isCommandRegistered(String arg1);

    /**
     * Gets all registered commands
     *
     * @return A list containing all registered commands
     */
    public abstract List<String> getCommandsAsString();

    /**
     * Dispatches a command using the default command manager
     *
     * @param arg1 The command which should be dispatched
     * @return If the execution was successful
     */
    public abstract boolean dispatchCommand(String arg1);

    /**
     * Creates a new command sender with specific permissions
     *
     * @param arg1 The permissions of the new command sender
     * @return A default command sender
     */
    public abstract CommandSender newCommandSender(Map<String, Boolean> arg1);

    /**
     * Creates a new command manger
     *
     * @return A new instance of this class
     */
    public static AbstractCommandManager defaultCommandManager() {
        return new CommandManager();
    }
}
