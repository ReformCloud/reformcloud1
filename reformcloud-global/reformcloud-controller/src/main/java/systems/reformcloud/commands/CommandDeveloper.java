/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.info.ClientInfo;
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
        super("developer", "Some small developer tools", "reformcloud.commands.developer",
            new String[]{"dev"});
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length >= 3 && args[0].equalsIgnoreCase("debug")) {
            if (args.length == 4) {
                switch (args[2].toLowerCase()) {
                    case "server": {
                        ServerInfo serverInfo = ReformCloudController.getInstance()
                            .getServerInfo(args[3]);
                        if (serverInfo == null) {
                            commandSender.sendMessage(
                                ReformCloudController.getInstance().getLoadedLanguage()
                                    .getCommand_error_occurred()
                                    .replace("%message%", "Server isn't connected"));
                            return;
                        }

                        ReformCloudController.getInstance().getChannelHandler()
                            .sendPacketSynchronized(
                                serverInfo.getCloudProcess().getName(),
                                new PacketOutEnableDebug(args[1].equalsIgnoreCase("enable"))
                            );
                        if (args[1].equalsIgnoreCase("enable")) {
                            commandSender.sendMessage(
                                ReformCloudController.getInstance().getLoadedLanguage()
                                    .getCommand_developer_debug_enable()
                                    .replace("%name%", "§e" + serverInfo.getCloudProcess().getName() + "§r"));
                        } else {
                            commandSender.sendMessage(
                                ReformCloudController.getInstance().getLoadedLanguage()
                                    .getCommand_developer_debug_disable()
                                    .replace("%name%", "§e" + serverInfo.getCloudProcess().getName() + "§r"));
                        }
                        break;
                    }

                    case "proxy": {
                        ProxyInfo proxyInfo = ReformCloudController.getInstance()
                            .getProxyInfo(args[3]);
                        if (proxyInfo == null) {
                            commandSender.sendMessage(
                                ReformCloudController.getInstance().getLoadedLanguage()
                                    .getCommand_error_occurred()
                                    .replace("%message%", "Proxy isn't connected"));
                            return;
                        }

                        ReformCloudController.getInstance().getChannelHandler()
                            .sendPacketSynchronized(
                                proxyInfo.getCloudProcess().getName(),
                                new PacketOutEnableDebug(args[1].equalsIgnoreCase("enable"))
                            );
                        if (args[1].equalsIgnoreCase("enable")) {
                            commandSender.sendMessage(
                                ReformCloudController.getInstance().getLoadedLanguage()
                                    .getCommand_developer_debug_enable()
                                    .replace("%name%", "§e" + proxyInfo.getCloudProcess().getName() + "§r"));
                        } else {
                            commandSender.sendMessage(
                                ReformCloudController.getInstance().getLoadedLanguage()
                                    .getCommand_developer_debug_disable()
                                    .replace("%name%", "§e" + proxyInfo.getCloudProcess().getName() + "§r"));
                        }
                        break;
                    }

                    case "client": {
                        Client client = ReformCloudController.getInstance().getClient(args[3]);
                        if (client == null || client.getClientInfo() == null) {
                            commandSender.sendMessage(
                                ReformCloudController.getInstance().getLoadedLanguage()
                                    .getCommand_error_occurred()
                                    .replace("%message%", "Client isn't connected"));
                            return;
                        }

                        ReformCloudController.getInstance().getChannelHandler()
                            .sendPacketSynchronized(
                                client.getName(),
                                new PacketOutEnableDebug(args[1].equalsIgnoreCase("enable"))
                            );
                        if (args[1].equalsIgnoreCase("enable")) {
                            commandSender.sendMessage(
                                ReformCloudController.getInstance().getLoadedLanguage()
                                    .getCommand_developer_debug_enable()
                                    .replace("%name%", "§e" + client.getName() + "§r"));
                        } else {
                            commandSender.sendMessage(
                                ReformCloudController.getInstance().getLoadedLanguage()
                                    .getCommand_developer_debug_disable()
                                    .replace("%name%", "§e" + client.getName() + "§r"));
                        }
                        break;
                    }

                    default: {
                        commandSender.sendMessage(
                            "dev debug <enable, disable> <server, proxy, client> <name>");
                        commandSender
                            .sendMessage("dev debug <enable, disable> <controller, --all>");
                        commandSender.sendMessage("dev standby <enable, disable> client");
                        break;
                    }
                }
                return;
            }

            if (args[2].toLowerCase().equalsIgnoreCase("--all")) {
                boolean enable = args[1].equalsIgnoreCase("enable");
                ReformCloudController.getInstance().getColouredConsoleProvider().setDebug(enable);
                ReformCloudController.getInstance().getChannelHandler()
                    .sendToAllSynchronized(new PacketOutEnableDebug(enable));
                if (args[1].equalsIgnoreCase("enable")) {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_developer_debug_enable()
                            .replace("%name%", "§eeverywhere§r"));
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_developer_debug_disable()
                            .replace("%name%", "§eeverywhere§r"));
                }
                return;
            }

            if (args[2].toLowerCase().equalsIgnoreCase("controller")) {
                ReformCloudController.getInstance().getColouredConsoleProvider()
                    .setDebug(args[1].equalsIgnoreCase("enable"));
                if (args[1].equalsIgnoreCase("enable")) {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_developer_debug_enable()
                            .replace("%name%", "§eController§r"));
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_developer_debug_disable()
                            .replace("%name%", "§eController§r"));
                }
                return;
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("standby")) {
            Client client = ReformCloudController.getInstance().getClient(args[2]);
            if (client == null || client.getClientInfo() == null) {
                commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage()
                    .getCommand_error_occurred()
                    .replace("%message%", "Client isn't connected"));
                return;
            }

            ClientInfo clientInfo =
                ReformCloudController.getInstance().getClientInfos().get(client.getName());

            if (clientInfo.isReady() && args[1].equalsIgnoreCase("disable")) {
                commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage()
                    .getCommand_error_occurred()
                    .replace("%message%", "Client isn't in standby mode"));
                return;
            } else if (!clientInfo.isReady() && args[1].equalsIgnoreCase("enable")) {
                commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage()
                    .getCommand_error_occurred()
                    .replace("%message%", "Client is already in standby mode"));
                return;
            }

            ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                client.getName(), new PacketOutSyncStandby(args[1].equalsIgnoreCase("enable"))
            );
            clientInfo.setReady(args[1].equalsIgnoreCase("enable"));

            if (args[1].equalsIgnoreCase("enable")) {
                commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage()
                    .getCommand_developer_standby_enable()
                    .replace("%name%", "§e" + client.getName() + "§r"));
            } else {
                commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage()
                    .getCommand_developer_standby_disable()
                    .replace("%name%", "§e" + client.getName() + "§r"));
            }
            return;
        }

        commandSender.sendMessage("dev debug <enable, disable> <server, proxy, client> <name>");
        commandSender.sendMessage("dev debug <enable, disable> <controller, --all>");
        commandSender.sendMessage("dev standby <enable, disable> client");
    }
}
