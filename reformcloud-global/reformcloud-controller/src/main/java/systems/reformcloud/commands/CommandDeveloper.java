/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.out.PacketOutEnableDebug;
import systems.reformcloud.network.sync.out.PacketOutSyncStandby;

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
                                    .replace("%name%", serverInfo.getCloudProcess().getName()));
                        } else {
                            commandSender.sendMessage(
                                ReformCloudController.getInstance().getLoadedLanguage()
                                    .getCommand_developer_debug_disable()
                                    .replace("%name%", serverInfo.getCloudProcess().getName()));
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
                                    .replace("%name%", proxyInfo.getCloudProcess().getName()));
                        } else {
                            commandSender.sendMessage(
                                ReformCloudController.getInstance().getLoadedLanguage()
                                    .getCommand_developer_debug_disable()
                                    .replace("%name%", proxyInfo.getCloudProcess().getName()));
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
                                    .replace("%name%", client.getName()));
                        } else {
                            commandSender.sendMessage(
                                ReformCloudController.getInstance().getLoadedLanguage()
                                    .getCommand_developer_debug_disable()
                                    .replace("%name%", client.getName()));
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
                            .replace("%name%", "everywhere"));
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_developer_debug_disable()
                            .replace("%name%", "everywhere"));
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
                            .replace("%name%", "controller"));
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_developer_debug_disable()
                            .replace("%name%", "controller"));
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
                    .replace("%name%", client.getName()));
            } else {
                commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage()
                    .getCommand_developer_standby_disable()
                    .replace("%name%", client.getName()));
            }
            return;
        }

        commandSender.sendMessage("dev debug <enable, disable> <server, proxy, client> <name>");
        commandSender.sendMessage("dev debug <enable, disable> <controller, --all>");
        commandSender.sendMessage("dev standby <enable, disable> client");
    }

    @Override
    public List<String> complete(String commandLine, String[] args) {
        List<String> out = new LinkedList<>();

        if (args.length == 0) {
            out.addAll(asList("DEBUG", "STANDBY"));
        }

        if (args.length == 1) {
            out.addAll(asList("ENABLE", "DISABLE"));
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("STANDBY")) {
                out.addAll(clients());
            } else if (args[0].equalsIgnoreCase("DEBUG")) {
                out.addAll(asList("SERVER", "PROXY", "CLIENT", "CONTROLLER", "--all"));
            }
        }

        if (args.length == 3) {
            if (args[2].equalsIgnoreCase("SERVER")) {
                out.addAll(servers());
            } else if (args[2].equalsIgnoreCase("PROXY")) {
                out.addAll(proxies());
            } else if (args[2].equalsIgnoreCase("CLIENT")) {
                out.addAll(clients());
            }
        }

        return out;
    }

    private List<String> servers() {
        List<String> out = new LinkedList<>();
        ReformCloudController.getInstance().getAllRegisteredServers()
            .forEach(servers -> out.add(servers.getCloudProcess().getName()));
        Collections.sort(out);
        return out;
    }

    private List<String> proxies() {
        List<String> out = new LinkedList<>();
        ReformCloudController.getInstance().getAllRegisteredProxies()
            .forEach(proxies -> out.add(proxies.getCloudProcess().getName()));
        Collections.sort(out);
        return out;
    }

    private List<String> clients() {
        List<String> out = new LinkedList<>();
        ReformCloudController.getInstance().getAllConnectedClients()
            .forEach(client -> out.add(client.getName()));
        Collections.sort(out);
        return out;
    }

}
