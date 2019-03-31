/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.examples.command;

import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.commands.defaults.DefaultUserCommandSender;
import systems.reformcloud.commands.interfaces.CommandSender;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

public class CommandSenderExample implements CommandSender {
    /**
     * How do you want to send a message to this sender
     * Dont use {@link System#out}
     */
    @Override
    public void sendMessage(String message) {
        if (ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider() != null)
            ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().info(message);
        else
            throw new IllegalStateException("ReformCloudController Logger is null or not ready");
    }

    /**
     * Returns if a sender has a specific permission
     * You can use for example a list with the permissions
     * For more Information
     * @see DefaultUserCommandSender
     */
    @Override
    public boolean hasPermission(String permission) {
        return false;
    }
}
