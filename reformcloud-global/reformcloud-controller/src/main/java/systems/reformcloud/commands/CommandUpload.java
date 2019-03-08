/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 08.03.2019
 */

public final class CommandUpload extends Command implements Serializable {
    public CommandUpload() {
        super("upload", "Uploads the given file to the specific position", "reformcloud.command.upload", new String[0]);
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length >= 1) {
            commandSender.sendMessage("upload <CONTROLLER, CLIENTS> URL");
            commandSender.sendMessage("upload PLUGIN <GLOBAL, GROUPNAME> NAME URL");
            commandSender.sendMessage("upload PLUGIN GROUPNAME TEMPLATE NAME URL");
            commandSender.sendMessage("upload <CONTROLLERADDON, CLIENTADDON> NAME URL");
            return;
        }


    }
}
