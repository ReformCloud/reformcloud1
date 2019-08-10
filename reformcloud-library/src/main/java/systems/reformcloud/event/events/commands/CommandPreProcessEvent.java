/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.commands;

import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.event.utility.Cancellable;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 09.08.2019
 */

public final class CommandPreProcessEvent extends CommandEvent implements Serializable, Cancellable {

    public CommandPreProcessEvent(Command command, CommandSender commandSender) {
        super(command);
        this.commandSender = commandSender;
    }

    private final CommandSender commandSender;

    public CommandSender getCommandSender() {
        return commandSender;
    }

    private boolean cancelled;

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
