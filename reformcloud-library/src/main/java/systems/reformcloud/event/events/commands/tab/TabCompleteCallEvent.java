/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.commands.tab;

import systems.reformcloud.commands.abstracts.AbstractCommandCompleter;
import systems.reformcloud.event.utility.Event;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public final class TabCompleteCallEvent extends Event implements Serializable {

    public TabCompleteCallEvent(AbstractCommandCompleter tabCompleter, String commandLine) {
        this.tabCompleter = tabCompleter;
        this.commandLine = commandLine;
    }

    private final AbstractCommandCompleter tabCompleter;

    private final String commandLine;

    public AbstractCommandCompleter getTabCompleter() {
        return tabCompleter;
    }

    public String getCommandLine() {
        return commandLine;
    }
}
