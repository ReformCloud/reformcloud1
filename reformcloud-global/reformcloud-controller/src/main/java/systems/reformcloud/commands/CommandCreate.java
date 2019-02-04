/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.proxy.defaults.DefaultProxyGroup;
import systems.reformcloud.meta.server.defaults.DefaultGroup;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public final class CommandCreate implements Command {
    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length < 2) {
            commandSender.sendMessage("create CLIENT <name> <ip>");
            commandSender.sendMessage("create SERVERGROUP <name> <client>");
            commandSender.sendMessage("create PROXYGROUP <name> <client>");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "client": {
                if (args.length != 3) {
                    commandSender.sendMessage("create CLIENT <name> <ip>");
                    return;
                }

                if (ReformCloudController.getInstance().getInternalCloudNetwork().getClients().get(args[1]) != null) {
                    commandSender.sendMessage("Client already exists.");
                    return;
                }

                commandSender.sendMessage("Trying to create new Client...");
                ReformCloudController.getInstance().getCloudConfiguration().createClient(new Client(args[1], args[2], null));
                break;
            }
            case "servergroup": {
                if (args.length != 3) {
                    commandSender.sendMessage("create SERVERGROUP <name> <client>");
                    return;
                }

                if (ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups().get(args[1]) != null) {
                    commandSender.sendMessage("ServerGroup already exists.");
                    return;
                }

                commandSender.sendMessage("Trying to create new ServerGroup...");
                ReformCloudController.getInstance().getCloudConfiguration().createServerGroup(new DefaultGroup(args[1], args[2]));
                break;
            }
            case "proxygroup": {
                if (args.length != 3) {
                    commandSender.sendMessage("create PROXYGROUP <name> <client>");
                    return;
                }

                if (ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().get(args[1]) != null) {
                    commandSender.sendMessage("ProxyGroup already exists.");
                    return;
                }

                commandSender.sendMessage("Trying to create new ProxyGroup...");
                ReformCloudController.getInstance().getCloudConfiguration().createProxyGroup(new DefaultProxyGroup(args[1], args[2]));
                break;
            }
            default:
                commandSender.sendMessage("create CLIENT <name> <ip>");
                commandSender.sendMessage("create SERVERGROUP <name> <client>");
                commandSender.sendMessage("create PROXYGROUP <name> <client>");
        }
    }

    @Override
    public String getPermission() {
        return "reformcloud.command.create";
    }
}
