/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands.ingame;

import systems.reformcloud.commands.ingame.command.IngameCommand;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author _Klaro | Pasqual K. / created on 19.03.2019
 */

public abstract class IngameCommandManger implements Serializable {
    public static AtomicReference<IngameCommandManger> instance = new AtomicReference<>();

    public abstract List<IngameCommand> getAllRegisteredCommands();

    public abstract void registerCommand(IngameCommand ingameCommand);

    public abstract void unregisterCommand(IngameCommand ingameCommand);

    public abstract void unregisterCommand(String name);

    public abstract IngameCommand getCommand(String name);
}
