/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.commands.defaults.DefaultConsoleCommandSender;
import systems.reformcloud.commands.defaults.DefaultUserCommandSender;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.annotiations.MayNotBePresent;

import java.io.Serializable;
import java.util.*;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

public final class CommandManager extends AbstractCommandManager implements Serializable {

    /**
     * The default cloud command sender
     */
    private final DefaultConsoleCommandSender defaultConsoleCommandSender = new DefaultConsoleCommandSender();

    /**
     * The command list, where all commands are located in
     */
    private List<Command> commands = new ArrayList<>();

    /**
     * Dispatches a specific command with the given command sender for security reasons
     *
     * @param commandSender The defaultConsoleCommandSender who send the command
     * @param command The command as string which was send
     * @return if the command is registered or not
     */
    @Override
    public boolean dispatchCommand(systems.reformcloud.commands.utility.CommandSender commandSender,
        String command) {
        String[] strings = command.split(" ");

        if (strings.length <= 0) {
            return false;
        }

        Command command2;
        Optional<Command> cmd = commands.stream().filter(command1 ->
            command1.getName().equalsIgnoreCase(strings[0]) || Arrays.asList(command1.getAliases())
                .contains(strings[0])
        ).findFirst();
        if (cmd.isPresent()) {
            command2 = cmd.get();
        } else {
            return false;
        }

        if (command2.getPermission() == null || commandSender
            .hasPermission(command2.getPermission())) {
            String string = command
                .replace((command.contains(" ") ? command.split(" ")[0] + " " : command), "");
            try {
                if (string.equalsIgnoreCase("")) {
                    command2.executeCommand(commandSender, new String[0]);
                } else {
                    final String[] arguments = string.split(" ");
                    command2.executeCommand(commandSender, arguments);
                }
            } catch (final Throwable throwable) {
                StringUtil
                    .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                        "Error while dispatching command", throwable);
            }
            return true;
        } else {
            return false;
        }
    }


    /**
     * Registers a specific command
     *
     * @param command The command which should be registered
     * @return The class instance
     */
    @Override
    public AbstractCommandManager registerCommand(Command command) {
        this.commands.add(command);
        return this;
    }

    @Override
    public List<Command> commands() {
        return commands;
    }

    /**
     * Unregisters all commands
     */
    @Override
    public void clearCommands() {
        this.commands.clear();
    }

    @Override
    @MayNotBePresent
    public Command findCommand(String commandLine) {
        if (commandLine == null || commandLine.isEmpty()) {
            return null;
        }

        for (Command command : commands) {
            if (command.getName().toLowerCase().startsWith(commandLine.toLowerCase())) {
                return command;
            }

            for (String alias : command.getAliases()) {
                if (alias.toLowerCase().startsWith(commandLine.toLowerCase())) {
                    return command;
                }
            }
        }

        return null;
    }

    /**
     * Unregisters an specific command
     *
     * @param name The name of the command which should be unregistered
     */
    @Override
    public void unregisterCommand(final String name) {
        this.commands.remove(findCommand(name));
    }

    /**
     * Checks if a command is registered
     *
     * @param command The command as string which should be checked
     * @return if the command is registered
     */
    @Override
    public boolean isCommandRegistered(final String command) {
        return this.commands.stream().anyMatch(
            e -> e.getName().equalsIgnoreCase(command) || Arrays.asList(e.getAliases())
                .contains(command));
    }

    /**
     * Gets all registered Commands
     *
     * @return a list with all registered commands as string
     */
    @Override
    public List<String> getCommandsAsString() {
        List<String> commands = new ArrayList<>();
        this.commands.forEach(e -> commands.add(e.getName()));
        this.commands.forEach(e -> commands.addAll(Arrays.asList(e.getAliases())));

        return commands;
    }

    /**
     * Dispatch a command with the default {@link DefaultConsoleCommandSender}
     *
     * @param command The command as string
     * @return if the command is registered
     */
    @Override
    public boolean dispatchCommand(String command) {
        return this.dispatchCommand(this.defaultConsoleCommandSender, command);
    }

    /**
     * Creates a new {@link DefaultUserCommandSender}
     *
     * @param permissions The permissions of the command sender
     * @return a new command sender
     */
    @Override
    public systems.reformcloud.commands.utility.CommandSender newCommandSender(
        final Map<String, Boolean> permissions) {
        return new DefaultUserCommandSender(permissions);
    }

    public List<Command> getCommands() {
        return this.commands;
    }
}
