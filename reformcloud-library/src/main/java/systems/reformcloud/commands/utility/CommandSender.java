/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands.utility;

import systems.reformcloud.logging.AbstractLoggerProvider;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

public interface CommandSender {

    /**
     * Sends a message to the command sender
     *
     * @param message The message that should be sent
     */
    default void sendMessage(String message) {
        AbstractLoggerProvider.defaultLogger().info().accept(message);
    }

    /**
     * Checks if the command sender has the given permission
     *
     * @param permission The permission which should be checked
     * @return If the command sender has the permission
     */
    boolean hasPermission(String permission);
}
