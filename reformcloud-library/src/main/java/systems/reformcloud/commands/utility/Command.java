/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands.utility;

import systems.reformcloud.commands.completer.TabCompleter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

public abstract class Command implements TabCompleter, Serializable {

    /**
     * The name of the command
     */
    private String name;

    /**
     * The description of the command
     */
    private String description;

    /**
     * The needed permission to execute the command
     */
    private String permission;

    /**
     * The aliases of the command
     */
    private String[] aliases;

    @java.beans.ConstructorProperties({"name", "description", "permission", "aliases"})
    protected Command(String name, String description, String permission, String[] aliases) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.aliases = aliases;
    }

    /**
     * Executes the command
     *
     * @param commandSender The command sender who sent the command
     * @param args The given command arguments
     */
    public abstract void executeCommand(CommandSender commandSender, String[] args);

    @Override
    public List<String> complete(String commandLine, String[] args) {
        return new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPermission() {
        return this.permission;
    }

    public String[] getAliases() {
        return this.aliases;
    }
}
