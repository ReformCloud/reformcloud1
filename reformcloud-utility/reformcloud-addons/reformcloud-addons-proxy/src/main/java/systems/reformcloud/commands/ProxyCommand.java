/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ProxyAddon;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 06.04.2019
 */

public final class ProxyCommand extends Command implements Serializable {
    public ProxyCommand() {
        super("proxy", "Manage the settings of a specific proxygroup", "reformcloud.commands.proxy", new String[0]);
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            ProxyAddon.getInstance().getProxyAddonConfiguration().reload();
        } else if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
            if (!ProxyAddon.getInstance().getProxyAddonConfiguration().createForProxy(args[1]))
                commandSender.sendMessage("The config already exists");
            else
                commandSender.sendMessage("The config for the proxy was created ");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
            if (!ProxyAddon.getInstance().getProxyAddonConfiguration().deleteForProxy(args[1]))
                commandSender.sendMessage("The config doesn't exists");
            else
                commandSender.sendMessage("The config for the proxy was deleted");
        } else {
            commandSender.sendMessage("proxy reload");
            commandSender.sendMessage("proxy <create/delete> NAME");
        }
    }
}
