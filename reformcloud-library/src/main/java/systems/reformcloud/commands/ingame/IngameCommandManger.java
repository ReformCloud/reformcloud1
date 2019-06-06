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

    /**
     * The instance of the current command manger
     */
    public static AtomicReference<IngameCommandManger> instance = new AtomicReference<>();

    /**
     * Gets a list of all registered commands
     *
     * @return A list containing all registered ingame commands
     */
    public abstract List<IngameCommand> getAllRegisteredCommands();

    /**
     * Registers a command
     *
     * @param ingameCommand The ingame command which should be registered
     */
    public abstract void registerCommand(IngameCommand ingameCommand);

    /**
     * Unregisters a command
     *
     * @param ingameCommand The ingame command which should be unregistered
     */
    public abstract void unregisterCommand(IngameCommand ingameCommand);

    /**
     * Unregisters a command
     *
     * @param name The name of the ingame command which should be unregistered
     */
    public abstract void unregisterCommand(String name);

    /**
     * Get a command which is registered
     *
     * @param name The name of the command
     * @return The registered command
     */
    public abstract IngameCommand getCommand(String name);
}
