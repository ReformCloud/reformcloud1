/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 15.04.2019
 */

public abstract class AbstractCommandManager implements Serializable {
    public abstract boolean dispatchCommand(CommandSender commandSender, String command);

    public abstract AbstractCommandManager registerCommand(Command command);

    public abstract void clearCommands();

    public abstract void unregisterCommand(String command);

    public abstract boolean isCommandRegistered(String command);

    public abstract List<String> getCommandsAsString();

    public abstract boolean dispatchCommand(String command);

    public AbstractCommandManager defaultCommandManager() {
        return new CommandManager();
    }
}
