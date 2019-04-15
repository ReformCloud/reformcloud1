/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands.defaults;

import systems.reformcloud.ReformCloudLibraryServiceProvider;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

public class CommandSender implements systems.reformcloud.commands.interfaces.CommandSender {
    @Override
    public void sendMessage(String message) {
        ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().info(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }
}
