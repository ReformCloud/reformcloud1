/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.utility.StringUtil;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 15.04.2019
 */

public final class CommandClear extends Command implements Serializable {
    public CommandClear() {
        super("clear", "Clears the console", "reformcloud.command.clear", new String[0]);
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        try {
            ReformCloudClient.getInstance().getLoggerProvider().getConsoleReader().clearScreen();
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error in clear command", ex);
        }
    }
}
