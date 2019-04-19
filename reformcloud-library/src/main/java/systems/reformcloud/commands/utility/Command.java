/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

@AllArgsConstructor
@Getter
public abstract class Command implements Serializable {
    /**
     * Some general information about the command
     */
    private String name, description, permission;

    /**
     * The aliases of the command
     */
    private String[] aliases;

    /**
     * Executes the command
     *
     * @param commandSender     The command sender who sent the command
     * @param args              The given command arguments
     */
    public abstract void executeCommand(CommandSender commandSender, String[] args);
}
