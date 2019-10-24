/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.commands;

import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.event.utility.Event;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public abstract class CommandEvent extends Event implements Serializable {

    public CommandEvent(Command command) {
        this.command = command;
    }

    private final Command command;

    public Command getCommand() {
        return command;
    }
}
