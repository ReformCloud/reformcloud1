/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;
import systems.reformcloud.utility.screen.defaults.DefaultScreenHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 05.02.2019
 */

public final class CommandScreen implements Serializable, Command {
    private static final long serialVersionUID = -1318658988288270472L;

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        ReformCloudController.getInstance().getScreenSessionProvider().joinScreen("Lobby-01", new DefaultScreenHandler("Client-01", "server", "Lobby-01"));
    }

    @Override
    public String getPermission() {
        return "reformcloud.command.screen";
    }
}
