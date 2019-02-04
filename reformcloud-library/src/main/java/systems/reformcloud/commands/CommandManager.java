/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.commands.defaults.CommandSender;
import systems.reformcloud.commands.defaults.DefaultUserCommandSender;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.utility.StringUtil;

import java.util.Map;
import java.util.Set;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

/**
 * CommandManager handles all {@link Command}
 */
public class CommandManager {
    private final CommandSender commandSender = new CommandSender();
    private Map<String, Command> commandMap = ReformCloudLibraryService.concurrentHashMap();

    /**
     * Dispatch method, to dispatch command
     *
     * @param commandSender
     * @param command
     * @return if the command is registered or not
     */
    private boolean dispatchCommand(systems.reformcloud.commands.interfaces.CommandSender commandSender, String command) {
        String[] strings = command.split(" ");

        if (strings.length <= 0) return false;

        if (this.commandMap.containsKey(strings[0].toLowerCase()) && commandSender.hasPermission(this.commandMap.get(strings[0].toLowerCase()).getPermission())) {
            String string = command.replace((command.contains(" ") ? command.split(" ")[0] + " " : command), "");
            try {
                if (string.equalsIgnoreCase(""))
                    this.commandMap.get(strings[0].toLowerCase()).executeCommand(commandSender, new String[0]);
                else {
                    final String[] arguments = string.split(" ");
                    this.commandMap.get(strings[0].toLowerCase()).executeCommand(commandSender, arguments);
                }
            } catch (final Throwable throwable) {
                StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while dispatching command", throwable);
            }
            return true;
        } else
            return false;
    }

    /**
     * Register a command
     *
     * @param name
     * @param command
     * @return this
     */
    public CommandManager registerCommand(final String name, Command command) {
        this.registerCommand(name, command, new String[]{});
        return this;
    }

    /**
     * Register a command with aliases
     *
     * @param name
     * @param command
     * @param aliases
     * @return this
     */
    public CommandManager registerCommand(final String name, Command command, final String... aliases) {
        this.commandMap.put(name.toLowerCase(), command);

        if (aliases.length > 0)
            for (String alias : aliases)
                this.commandMap.put(alias.toLowerCase(), command);

        return this;
    }

    /**
     * Unregisters all commands
     */
    public void clearCommands() {
        this.commandMap.clear();
    }

    /**
     * Unregisters an specific command
     *
     * @param name
     */
    public void unregisterCommand(final String name) {
        this.commandMap.remove(name);
    }

    /**
     * Check if an command is registered
     *
     * @param command
     * @return if the command is registered
     */
    public boolean isCommandRegistered(final String command) {
        return this.commandMap.containsKey(command);
    }

    /**
     * Gets all registered Commands
     *
     * @return a Set with all commands as String
     */
    public Set<String> getCommands() {
        return this.commandMap.keySet();
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
