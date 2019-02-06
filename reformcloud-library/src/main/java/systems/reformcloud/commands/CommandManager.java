/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import lombok.Getter;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.commands.defaults.CommandSender;
import systems.reformcloud.commands.defaults.DefaultUserCommandSender;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.utility.StringUtil;

import java.util.*;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

/**
 * CommandManager handles all {@link Command}
 */
public class CommandManager {
    private final CommandSender commandSender = new CommandSender();

    @Getter
    private List<Command> commands = new ArrayList<>();

    /**
     * Dispatch method, to dispatch command
     *
     * @param commandSender
     * @param command
     * @return if the command is registered or not
     */
    private boolean dispatchCommand(systems.reformcloud.commands.interfaces.CommandSender commandSender, String command) {
        String[] strings = command.split(" ");

        if (strings.length <= 0)
            return false;

        Command command2;
        Optional<Command> cmd = commands.stream().filter(command1 ->
                command1.getName().equalsIgnoreCase(strings[0]) || Arrays.asList(command1.getAliases()).contains(strings[0])
        ).findFirst();
        if (cmd.isPresent())
            command2 = cmd.get();
        else
            return false;

        if (command2.getPermission() == null || commandSender.hasPermission(command2.getPermission())) {
            String string = command.replace((command.contains(" ") ? command.split(" ")[0] + " " : command), "");
            try {
                if (string.equalsIgnoreCase(""))
                    command2.executeCommand(commandSender, new String[0]);
                else {
                    final String[] arguments = string.split(" ");
                    command2.executeCommand(commandSender, arguments);
                }
            } catch (final Throwable throwable) {
                StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while dispatching command", throwable);
            }
            return true;
        } else
            return false;
    }


    /**
     * Register a command with aliases
     *
     * @param command
     * @return this
     */
    public CommandManager registerCommand(Command command) {
        this.commands.add(command);
        return this;
    }

    /**
     * Unregisters all commands
     */
    public void clearCommands() {
        this.commands.clear();
    }

    /**
     * Unregisters an specific command
     *
     * @param name
     */
    public void unregisterCommand(final String name) {
        this.commands.remove(name);
    }

    /**
     * Check if an command is registered
     *
     * @param command
     * @return if the command is registered
     */
    public boolean isCommandRegistered(final String command) {
        return this.commands.stream().anyMatch(e -> e.getName().equalsIgnoreCase(command) || Arrays.asList(e.getAliases()).contains(command));
    }

    /**
     * Gets all registered Commands
     *
     * @return a Set with all commands as String
     */
    public List<String> getCommandsAsString() {
        List<String> commands = new ArrayList<>();
        this.commands.forEach(e -> commands.add(e.getName()));
        this.commands.forEach(e -> commands.addAll(Arrays.asList(e.getAliases())));

        return commands;
    }

    /**
     * Dispatch a command with the default {@link CommandSender}
     *
     * @param command
     * @return if the command is registered
     */
    public boolean dispatchCommand(String command) {
        return this.dispatchCommand(this.commandSender, command);
    }

    /**
     * Creates a new {@link DefaultUserCommandSender}
     *
     * @param permissions
     * @return new {@link DefaultUserCommandSender} with given permissions
     */
    public systems.reformcloud.commands.interfaces.CommandSender newCommandSender(final Map<String, Boolean> permissions) {
        return new DefaultUserCommandSender(permissions);
    }
}
