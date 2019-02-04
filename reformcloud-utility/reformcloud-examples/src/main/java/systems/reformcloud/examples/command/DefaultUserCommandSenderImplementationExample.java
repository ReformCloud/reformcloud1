/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.examples.command;

import systems.reformcloud.commands.defaults.DefaultUserCommandSender;
import systems.reformcloud.commands.interfaces.CommandSender;

import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

public class DefaultUserCommandSenderImplementationExample extends DefaultUserCommandSender {
    /**
     * Creates a new {@link CommandSender} with the given permissions
     * The {@link Map<String, Boolean>} because a permission can be given or revoked
     *                      Example: * : true ; but you don't want the exit command
     *                              reformcloud.command.exit : false
     * If the user has {@code "*"} permissions, he has all permissions
     */
    public DefaultUserCommandSenderImplementationExample(Map<String, Boolean> permissions) {
        super(permissions); //Use for a example a ConcurrentHashMap
    }

    /**
     * For documentation see {@link CommandSenderExample}
     *
     * You don't have to implement that ; Make sure you know, that reformcloud permission check is now revoked (If you don't use the super call)
     */
    @Override
    public boolean hasPermission(String permission) {
        return super.hasPermission(permission);
    }

    /**
     * For documentation see {@link CommandSenderExample}
     *
     * You don't have to implement that ; Make sure you know, that reformcloud message send is now revoked (If you don't use the super call)
     */
    @Override
    public void sendMessage(String message) {
        super.sendMessage(message);
    }
}
