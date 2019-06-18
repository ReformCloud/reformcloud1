/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands.defaults;

import systems.reformcloud.commands.AbstractCommandManager;
import systems.reformcloud.commands.abstracts.CommandMap;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.utility.Require;
import systems.reformcloud.utility.annotiations.MayNotBePresent;
import systems.reformcloud.utility.annotiations.ShouldNotBeNull;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author _Klaro | Pasqual K. / created on 18.06.2019
 */

public final class DefaultCommandMap extends CommandMap implements Serializable {

    private AbstractCommandManager commandManager;

    @ConstructorProperties({"abstractCommandManager"})
    public DefaultCommandMap(AbstractCommandManager abstractCommandManager) {
        Require.requireNotNull(abstractCommandManager);
        this.commandManager = abstractCommandManager;
    }

    @Override
    @MayNotBePresent
    public Command fromFirstArgument(@ShouldNotBeNull String commandLine) {
        Require.requireNotNull(commandLine);
        String[] split = commandLine.split(" ");
        return split.length >= 1 ? commandManager.findCommand(split[0]) : null;
    }

    @Override
    @ShouldNotBeNull
    public List<Command> findAll(@ShouldNotBeNull String currentBuffer) {
        List<Command> all = commandManager.commands();
        if (currentBuffer.isEmpty()) {
            return all;
        }

        return all.stream().filter(e -> e.getName().toLowerCase().startsWith(currentBuffer))
            .collect(Collectors.toList());
    }

    @Override
    public AbstractCommandManager commandManager() {
        return commandManager;
    }
}
