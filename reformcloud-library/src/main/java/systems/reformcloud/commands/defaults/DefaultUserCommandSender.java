/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands.defaults;

import lombok.AllArgsConstructor;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.commands.interfaces.CommandSender;

import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

/**
 * User CommandSender Class to create easier {@link CommandSender}
 */

@AllArgsConstructor
public class DefaultUserCommandSender implements CommandSender {
    private Map<String, Boolean> permissions;

    @Override
    public void sendMessage(String message) {
        ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().info(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        if (this.permissions.containsKey("*") && this.permissions.get("*"))
            return true;

        if (!this.permissions.containsKey(permission))
            return false;

        return this.permissions.get(permission);
    }
}
