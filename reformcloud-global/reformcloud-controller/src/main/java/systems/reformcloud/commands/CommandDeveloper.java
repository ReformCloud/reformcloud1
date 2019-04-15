/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.out.PacketOutEnableDebug;
import systems.reformcloud.network.sync.out.PacketOutSyncStandby;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 15.04.2019
 */

public final class CommandDeveloper extends Command implements Serializable {
    public CommandDeveloper() {
        super("developer", "Some small developer tools", "reformcloud.commands.developer", new String[]{"dev"});
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length >= 3 && args[0].equalsIgnoreCase("debug")) {
            if (args.length == 4) {
                switch (args[2].toLowerCase()) {
                    case "server": {
                        ServerInfo serverInfo = ReformCloudController.getInstance().getServerInfo(args[3]);
                        if (serverInfo == null) {
                            commandSender.sendMessage("Server isn't connected");
                            return;
                        }

                        ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                                serverInfo.getCloudProcess().getName(),
                                new PacketOutEnableDebug(args[1].equalsIgnoreCase("enable"))
                        );
                        commandSender.sendMessage(
                                (args[1].equalsIgnoreCase("enable") ? "§aEnabled" : "§cDisabled") + " §rdebug on "
                                        + serverInfo.getCloudProcess().getName()
                        );
                        break;
                    }

                    case "proxy": {
                        ProxyInfo proxyInfo = ReformCloudController.getInstance().getProxyInfo(args[3]);
                        if (proxyInfo == null) {
                            commandSender.sendMessage("Proxy isn't connected");
                            return;
                        }

                        ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                                proxyInfo.getCloudProcess().getName(),
                                new PacketOutEnableDebug(args[1].equalsIgnoreCase("enable"))
                        );
                        commandSender.sendMessage(
                                (args[1].equalsIgnoreCase("enable") ? "§aEnabled" : "§cDisabled") + " §rdebug on "
                                        + proxyInfo.getCloudProcess().getName()
                        );
                        break;
                    }

                    case "client": {
                        Client client = ReformCloudController.getInstance().getClient(args[3]);
                        if (client == null || client.getClientInfo() == null) {
                            commandSender.sendMessage("Client isn't connected");
                            return;
                        }

                        ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                                client.getName(),
                                new PacketOutEnableDebug(args[1].equalsIgnoreCase("enable"))
                        );
                        commandSender.sendMessage(
                                (args[1].equalsIgnoreCase("enable") ? "§aEnabled" : "§cDisabled") + " §rdebug on "
                                        + client.getName()
                        );
                        break;
                    }

                    case "controller": {
                        ReformCloudController.getInstance().getLoggerProvider().setDebug(args[1].equalsIgnoreCase("enable"));
                        commandSender.sendMessage(
                                (args[1].equalsIgnoreCase("enable") ? "§aEnabled" : "§cDisabled") + "§r debug in controller"
                        );
                        break;
                    }

                    default: {
                        commandSender.sendMessage("dev debug <enable, disable> <server, proxy, client, controller> <name>");
                        commandSender.sendMessage("dev debug <enable, disable> --all");
                    }
                }
                return;
            }

            if (args[2].toLowerCase().equalsIgnoreCase("--all")) {
                boolean enable = args[1].equalsIgnoreCase("enable");
                ReformCloudController.getInstance().getLoggerProvider().setDebug(enable);
                ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(new PacketOutEnableDebug(enable));
                commandSender.sendMessage("Trying to enable debug everywhere...");
                return;
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("standby")) {
            Client client = ReformCloudController.getInstance().getClient(args[2]);
            if (client == null || client.getClientInfo() == null) {
                commandSender.sendMessage("Client isn't connected");
                return;
            }

            if (client.getClientInfo().isReady() && args[1].equalsIgnoreCase("disable")) {
                commandSender.sendMessage("Client isn't in standby mode");
                return;
            } else if (!client.getClientInfo().isReady() && args[1].equalsIgnoreCase("enable")) {
                commandSender.sendMessage("Client is already in standby mode");
                return;
            }

            ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                    client.getName(), new PacketOutSyncStandby(args[1].equalsIgnoreCase("enable"))
            );
            commandSender.sendMessage(
                    (args[1].equalsIgnoreCase("enable") ? "§aEnabling" : "§cDisabling") + "§r standby mode on client " + client.getName()
            );
        }

        commandSender.sendMessage("dev debug <enable, disable> <server, proxy, client, controller> <name>");
        commandSender.sendMessage("dev debug <enable, disable> --all");
        commandSender.sendMessage("dev standby <enable, disable> name");
    }
}
