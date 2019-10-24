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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
    public List<String> findAll(@ShouldNotBeNull String currentBuffer) {
        List<Command> all = commandManager.commands();
        if (currentBuffer.isEmpty()) {
            List<String> preOut = new ArrayList<>();
            all.forEach(e -> preOut.add(e.getName()));
            return preOut;
        }

        List<String> out = new LinkedList<>();
        all.forEach(e -> {
            if (e.getName().toLowerCase().startsWith(currentBuffer.toLowerCase())) {
                out.add(e.getName());
            }

            Arrays.stream(e.getAliases()).forEach(alias -> {
                if (alias.toLowerCase().startsWith(currentBuffer.toLowerCase())) {
                    out.add(alias);
                }
            });
        });
        return out;
    }

    @Override
    public AbstractCommandManager commandManager() {
        return commandManager;
    }
}
