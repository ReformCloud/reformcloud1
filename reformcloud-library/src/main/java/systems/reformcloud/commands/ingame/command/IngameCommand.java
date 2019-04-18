/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands.ingame.command;

import lombok.Getter;
import systems.reformcloud.commands.ingame.sender.IngameCommandSender;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 19.03.2019
 */

@Getter
public abstract class IngameCommand implements Serializable {
    /**
     * The name of the ingame command
     */
    private String name;

    /**
     * The permission which is needed to use the command
     */
    private String permission;

    /**
     * The aliases of the command
     */
    private String[] aliases;

    /**
     * Creates a new ingame command
     *
     * @param name      The name of the command
     */
    public IngameCommand(String name) {
        this.name = name;
        this.aliases = new String[0];
    }

    /**
     * Creates a new ingame command
     *
     * @param name              The name of the command
     * @param permission        The permission of the command
     * @param aliases           The aliases of the command
     */
    public IngameCommand(String name, String permission, String[] aliases) {
        this.name = name;
        this.permission = permission;
        this.aliases = aliases;
    }

    /**
     * Handles the command
     *
     * @param commandSender     The command sender who send the command
     * @param args              The arguments provided with the command
     */
    public abstract void handle(IngameCommandSender commandSender, String[] args);
}
