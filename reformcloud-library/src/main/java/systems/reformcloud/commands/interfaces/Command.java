/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands.interfaces;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

/**
 * Command interface to create a custom command
 */
public interface Command {
    void executeCommand(CommandSender commandSender, String[] args);

    String getPermission();
}
