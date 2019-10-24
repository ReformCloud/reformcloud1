/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.commands;

import systems.reformcloud.commands.utility.Command;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public final class CommandRegisterEvent extends CommandEvent implements Serializable {

    public CommandRegisterEvent(Command command) {
        super(command);
    }
}
