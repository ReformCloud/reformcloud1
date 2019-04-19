/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands.defaults;

import lombok.AllArgsConstructor;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.logging.AbstractLoggerProvider;

import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

@AllArgsConstructor
public class DefaultUserCommandSender implements CommandSender {
    /**
     * The permissions of the command sender
     */
    private Map<String, Boolean> permissions;

    /**
     * Sends a message to the console
     *
     * @param message       The message which should be sent
     */
    @Override
    public void sendMessage(String message) {
        AbstractLoggerProvider.defaultLogger().info().accept(message);
    }

    /**
     * Checks if the command sender has the given permission
     *
     * @param permission        The permission which should be checked
     * @return                  If the command sender has the permission
     */
    @Override
    public boolean hasPermission(String permission) {
        if (this.permissions.containsKey("*") && this.permissions.get("*"))
            return true;

        if (!this.permissions.containsKey(permission))
            return false;

        return this.permissions.get(permission);
    }
}
