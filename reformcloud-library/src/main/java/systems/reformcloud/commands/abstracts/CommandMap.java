/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands.abstracts;

import systems.reformcloud.commands.AbstractCommandManager;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.utility.annotiations.MayNotBePresent;
import systems.reformcloud.utility.annotiations.ShouldNotBeNull;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 18.06.2019
 */

public abstract class CommandMap implements Serializable {

    /**
     * Finds a command from the full command line given
     *
     * @param arg1 The command line which was typed yet
     * @return The command or {@code null} if the command is not registered
     */
    @MayNotBePresent
    public abstract Command fromFirstArgument(@ShouldNotBeNull String arg1);

    /**
     * Filters all commands if they are a command which got searched
     *
     * @param arg1 The current command line state
     * @return A list containing all command candidates
     */
    @ShouldNotBeNull
    public abstract List<String> findAll(@ShouldNotBeNull String arg1);

    /**
     * The used command manager
     *
     * @return The current command manager
     */
    public abstract AbstractCommandManager commandManager();
}
