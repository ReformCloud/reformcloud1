/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.utility.StringUtil;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public final class CommandClear extends Command implements Serializable {

    public CommandClear() {
        super("clear", "Clears the console", "reformcloud.command.clear", new String[0]);
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        try {
            ReformCloudController.getInstance().getColouredConsoleProvider().getConsoleReader()
                .clearScreen();
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Error in clear command", ex);
        }
    }
}
