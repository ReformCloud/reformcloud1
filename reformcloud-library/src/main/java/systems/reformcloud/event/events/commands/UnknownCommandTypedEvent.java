/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.commands;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 09.08.2019
 */

public final class UnknownCommandTypedEvent extends CommandEvent implements Serializable {

    public UnknownCommandTypedEvent(String commandLine) {
        super(null);
        this.commandLine = commandLine;
    }

    private final String commandLine;

    public String getCommandLine() {
        return commandLine;
    }
}
