/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.language.utility.Language;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.network.out.PacketOutGetClientProcessQueue;
import systems.reformcloud.network.out.PacketOutRemoveProxyQueueProcess;
import systems.reformcloud.network.out.PacketOutRemoveServerQueueProcess;
import systems.reformcloud.network.out.PacketOutStartGameServer;
import systems.reformcloud.network.out.PacketOutStartProxy;
import systems.reformcloud.network.out.PacketOutStopProcess;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public final class CommandProcess extends Command implements Serializable {

    private final DecimalFormat decimalFormat = new DecimalFormat("##.###");

    private final Language language = ReformCloudController.getInstance().getLoadedLanguage();

    public CommandProcess() {
        super("process", "Main command for all processes", "reformcloud.command.process",
            new String[0]);
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length < 1) {
            commandSender.sendMessage("process stop <name/--all/--empty>");
            commandSender.sendMessage("process restart <name>");
            commandSender.sendMessage("process stopGroup <group>");
            commandSender.sendMessage("process start <group-name> <number>");
            commandSender.sendMessage("process list");
            commandSender.sendMessage("process list <server/proxy> <name>");
            commandSender.sendMessage("");
            commandSender.sendMessage("process queue <client> list");
            commandSender.sendMessage("process queue <client> remove <proxy/server> <name>");
            return;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("queue") && args[2]
            .equalsIgnoreCase("list")) {
            Client client = ReformCloudController.getInstance().getInternalCloudNetwork()
                .getClients().get(args[1]);
            if (client == null || client.getClientInfo() == null) {
                commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage()
                    .getCommand_error_occurred()
                    .replace("%message%", "The client isn't registered"));
                return;
            }

            ReformCloudController.getInstance().getChannelHandler()
                .sendPacketAsynchronous(client.getName(), new PacketOutGetClientProcessQueue());
            commandSender.sendMessage(language.getCommand_process_queue_requested());
            return;
        } else if (args.length == 5
            && args[0].equalsIgnoreCase("queue")
            && args[2].equalsIgnoreCase("remove")) {
            Client client = ReformCloudController.getInstance().getInternalCloudNetwork()
                .getClients().get(args[1]);
            if (client == null || client.getClientInfo() == null) {
                commandSender.sendMessage("Client isn't registered in CloudController");
                return;
            }

            switch (args[3].toLowerCase()) {
                case "proxy": {
                    ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(
                        client.getName(), new PacketOutRemoveProxyQueueProcess(args[4])
                    );
                    ReformCloudController.getInstance().getCloudProcessOfferService()
                        .removeWaitingProcess(args[4]);
                    commandSender.sendMessage(language.getCommand_process_remove_queue_entry()
                        .replace("%name%", args[4]));
                    break;
                }

                case "server": {
                    ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(
                        client.getName(), new PacketOutRemoveServerQueueProcess(args[4])
                    );
                    ReformCloudController.getInstance().getCloudProcessOfferService()
                        .removeWaitingProcess(args[4]);
                    commandSender.sendMessage(language.getCommand_process_remove_queue_entry()
                        .replace("%name%", args[4]));
                    break;
                }

                default: {
                    commandSender.sendMessage("process stop <name/--all/--empty>");
                    commandSender.sendMessage("process restart <name>");
                    commandSender.sendMessage("process stopGroup <group>");
                    commandSender.sendMessage("process start <group-name> <number>");
                    commandSender.sendMessage("process list");
                    commandSender.sendMessage("process list <server/proxy> <name>");
                    commandSender.sendMessage("");
                    commandSender.sendMessage("process queue <client> list");
                    commandSender
                        .sendMessage("process queue <client> remove <proxy/server> <name>");
                }
            }

            return;
        } else if (args.length == 3
            && args[0].equalsIgnoreCase("start")
            && ReformCloudLibraryService.checkIsInteger(args[2])) {
            if (ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups()
                .containsKey(args[1])) {
                final ServerGroup serverGroup = ReformCloudController.getInstance()
                    .getInternalCloudNetwork().getServerGroups().get(args[1]);
                for (int i = 1; i <= Integer.valueOf(args[2]); i++) {
                    final Client client = ReformCloudController.getInstance()
                        .getBestClient(serverGroup.getClients(), serverGroup.getMemory());

                    if (client != null) {
                        final String id = Integer.toString(
                            ReformCloudController.getInstance().getCloudProcessOfferService()
                                .nextServerID(serverGroup.getName()));
                        final String name =
                            serverGroup.getName() + ReformCloudController.getInstance()
                                .getCloudConfiguration().getSplitter() + (Integer.parseInt(id) <= 9
                                ? "0" : "") + id;
                        ReformCloudController.getInstance().getCloudProcessOfferService()
                            .registerID(
                                serverGroup.getName(), name, Integer.valueOf(id)
                            );
                        ReformCloudController.getInstance().getChannelHandler()
                            .sendPacketSynchronized(client.getName(),
                                new PacketOutStartGameServer(serverGroup, name, UUID.randomUUID(),
                                    new Configuration(), id)
                            );
                        commandSender.sendMessage(language.getCommand_process_trying_startup());
                    } else {
                        commandSender.sendMessage(language.getNo_available_client_for_startup());
                    }
                    ReformCloudLibraryService.sleep(200);
                }
            } else if (ReformCloudController.getInstance().getInternalCloudNetwork()
                .getProxyGroups().containsKey(args[1])) {
                final ProxyGroup proxyGroup = ReformCloudController.getInstance()
                    .getInternalCloudNetwork().getProxyGroups().get(args[1]);
                for (int i = 1; i <= Integer.valueOf(args[2]); i++) {
                    final Client client = ReformCloudController.getInstance()
                        .getBestClient(proxyGroup.getClients(), proxyGroup.getMemory());

                    if (client != null) {
                        final String id = Integer.toString(
                            ReformCloudController.getInstance().getCloudProcessOfferService()
                                .nextProxyID(proxyGroup.getName()));
                        final String name =
                            proxyGroup.getName() + ReformCloudController.getInstance()
                                .getCloudConfiguration().getSplitter() + (Integer.parseInt(id) <= 9
                                ? "0" : "") + id;
                        ReformCloudController.getInstance().getCloudProcessOfferService()
                            .registerProxyID(proxyGroup.getName(), name, Integer.valueOf(id));
                        ReformCloudController.getInstance().getChannelHandler()
                            .sendPacketSynchronized(client.getName(),
                                new PacketOutStartProxy(proxyGroup, name, UUID.randomUUID(),
                                    new Configuration(), id)
                            );
                        commandSender.sendMessage(language.getCommand_process_trying_startup());
                    } else {
                        commandSender.sendMessage(language.getNo_available_client_for_startup());
                    }
                    ReformCloudLibraryService.sleep(200);
                }
            } else {
                commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage()
                    .getCommand_error_occurred()
                    .replace("%message%", "ServerGroup or ProxyGroup doesn't exists"));
            }
            return;
        }

        switch (args[0].toLowerCase()) {
            case "stop": {
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("--all")) {
                        ReformCloudController.getInstance()
                            .getInternalCloudNetwork()
                            .getServerProcessManager()
                            .getAllRegisteredProxyProcesses()
                            .forEach(info -> {
                                ReformCloudController.getInstance()
                                    .getChannelHandler()
                                    .sendPacketSynchronized(info.getCloudProcess().getClient(),
                                        new PacketOutStopProcess(info.getCloudProcess().getName()));
                                commandSender.sendMessage(language.getCommand_process_try_stop()
                                    .replace("%name%", info.getCloudProcess().getName()));
                                ReformCloudLibraryService.sleep(300);
                            });

                        ReformCloudController.getInstance()
                            .getInternalCloudNetwork()
                            .getServerProcessManager()
                            .getAllRegisteredServerProcesses()
                            .forEach(info -> {
                                ReformCloudController.getInstance()
                                    .getChannelHandler()
                                    .sendPacketSynchronized(info.getCloudProcess().getClient(),
                                        new PacketOutStopProcess(info.getCloudProcess().getName()));
                                commandSender.sendMessage(language.getCommand_process_try_stop()
                                    .replace("%name%", info.getCloudProcess().getName()));
                                ReformCloudLibraryService.sleep(400);
                            });
                        return;
                    }

                    if (args[1].equalsIgnoreCase("--empty")) {
                        ReformCloudController.getInstance()
                            .getInternalCloudNetwork()
                            .getServerProcessManager()
                            .getAllRegisteredProxyProcesses()
                            .stream()
                            .filter(info -> info.getOnline() == 0)
                            .forEach(info -> {
                                ReformCloudController.getInstance()
                                    .getChannelHandler()
                                    .sendPacketSynchronized(info.getCloudProcess().getClient(),
                                        new PacketOutStopProcess(info.getCloudProcess().getName()));
                                commandSender.sendMessage(language.getCommand_process_try_stop()
                                    .replace("%name%", info.getCloudProcess().getName()));
                                ReformCloudLibraryService.sleep(200);
                            });

                        ReformCloudController.getInstance()
                            .getInternalCloudNetwork()
                            .getServerProcessManager()
                            .getAllRegisteredServerProcesses()
                            .stream()
                            .filter(info -> info.getOnline() == 0)
                            .forEach(info -> {
                                ReformCloudController.getInstance()
                                    .getChannelHandler()
                                    .sendPacketSynchronized(info.getCloudProcess().getClient(),
                                        new PacketOutStopProcess(info.getCloudProcess().getName()));
                                commandSender.sendMessage(language.getCommand_process_try_stop()
                                    .replace("%name%", info.getCloudProcess().getName()));
                                ReformCloudLibraryService.sleep(200);
                            });
                        return;
                    }

                    if (ReformCloudController.getInstance().getInternalCloudNetwork()
                        .getServerProcessManager().getRegisteredProxyByName(args[1]) != null) {
                        final ProxyInfo proxyInfo = ReformCloudController.getInstance()
                            .getInternalCloudNetwork().getServerProcessManager()
                            .getRegisteredProxyByName(args[1]);
                        ReformCloudController.getInstance().getChannelHandler()
                            .sendPacketAsynchronous(proxyInfo.getCloudProcess().getClient(),
                                new PacketOutStopProcess(proxyInfo.getCloudProcess().getName()));
                        commandSender.sendMessage(language.getCommand_process_try_stop()
                            .replace("%name%", proxyInfo.getCloudProcess().getName()));
                    } else if (ReformCloudController.getInstance().getInternalCloudNetwork()
                        .getServerProcessManager().getRegisteredServerByName(args[1]) != null) {
                        final ServerInfo serverInfo = ReformCloudController.getInstance()
                            .getInternalCloudNetwork().getServerProcessManager()
                            .getRegisteredServerByName(args[1]);
                        ReformCloudController.getInstance().getChannelHandler()
                            .sendPacketAsynchronous(serverInfo.getCloudProcess().getClient(),
                                new PacketOutStopProcess(serverInfo.getCloudProcess().getName()));
                        commandSender.sendMessage(language.getCommand_process_try_stop()
                            .replace("%name%", serverInfo.getCloudProcess().getName()));
                    } else {
                        commandSender.sendMessage(
                            ReformCloudController.getInstance().getLoadedLanguage()
                                .getCommand_error_occurred()
                                .replace("%message%",
                                    "This Server or Proxy is not connected to controller"));
                    }
                    break;
                } else {
                    commandSender.sendMessage("process stop <name>");
                }
                break;
            }
            case "stopgroup": {
                if (args.length == 2) {
                    if (ReformCloudController.getInstance().getInternalCloudNetwork()
                        .getServerGroups().get(args[1]) != null) {
                        ReformCloudController.getInstance()
                            .getInternalCloudNetwork()
                            .getServerProcessManager()
                            .getAllRegisteredServerProcesses()
                            .stream()
                            .filter(e -> e.getServerGroup().getName().equals(args[1]))
                            .forEach(e -> {
                                ReformCloudController.getInstance().getChannelHandler()
                                    .sendPacketSynchronized(
                                        e.getCloudProcess().getClient(),
                                        new PacketOutStopProcess(e.getCloudProcess().getName())
                                    );
                                commandSender.sendMessage(language.getCommand_process_try_stop()
                                    .replace("%name%", e.getCloudProcess().getName()));
                                ReformCloudLibraryService.sleep(100);
                            });
                        break;
                    } else if (ReformCloudController.getInstance().getInternalCloudNetwork()
                        .getProxyGroups().get(args[1]) != null) {
                        ReformCloudController.getInstance()
                            .getInternalCloudNetwork()
                            .getServerProcessManager()
                            .getAllRegisteredProxyProcesses()
                            .stream()
                            .filter(e -> e.getProxyGroup().getName().equals(args[1]))
                            .forEach(e -> {
                                ReformCloudController.getInstance().getChannelHandler()
                                    .sendPacketSynchronized(
                                        e.getCloudProcess().getClient(),
                                        new PacketOutStopProcess(e.getCloudProcess().getName())
                                    );
                                commandSender.sendMessage(language.getCommand_process_try_stop()
                                    .replace("%name%", e.getCloudProcess().getName()));
                                ReformCloudLibraryService.sleep(100);
                            });
                        break;
                    } else {
                        commandSender.sendMessage(
                            ReformCloudController.getInstance().getLoadedLanguage()
                                .getCommand_error_occurred()
                                .replace("%message%",
                                    "This Server or Proxy is not connected to controller"));
                    }
                } else {
                    commandSender.sendMessage("process stopGroup <group>");
                }

                break;
            }
            case "list": {
                if (args.length != 3) {
                    List<Client> connectedClients = new ArrayList<>();
                    ReformCloudController.getInstance().getInternalCloudNetwork()
                        .getClients()
                        .values()
                        .stream()
                        .filter(client -> client.getClientInfo() != null)
                        .forEach(connectedClients::add);

                    commandSender.sendMessage("The following Clients are connected: ");
                    connectedClients.forEach(e -> {
                        ClientInfo clientInfo = ReformCloudController.getInstance().getClientInfos()
                            .get(e.getName());
                        commandSender.sendMessage(
                            "    - " + e.getName() + " | Host=" + e.getIp() + " | Memory-Usage="
                                + clientInfo.getUsedMemory() + "MB/" + clientInfo.getMaxMemory()
                                + "MB | Processors: " + clientInfo.getCpuCoresSystem()
                                + " | CPU-Usage: " + decimalFormat.format(clientInfo.getCpuUsage())
                                + "% | Started-Processes: " + (clientInfo.getStartedProxies().size()
                                + clientInfo.getStartedServers().size()));
                        ReformCloudController.getInstance().getLoggerProvider().emptyLine();
                        commandSender.sendMessage(
                            "The following proxies are started on \"" + e.getName() + "\": ");
                        ReformCloudController.getInstance().getInternalCloudNetwork()
                            .getServerProcessManager().getAllRegisteredProxyProcesses().stream()
                            .filter(proxyInfo -> proxyInfo.getCloudProcess().getClient()
                                .equals(e.getName())).forEach(info -> commandSender.sendMessage(
                            "    - " + info.getCloudProcess().getName() + " | Player=" + info
                                .getOnline() + "/" + info.getProxyGroup().getMaxPlayers()));
                        ReformCloudController.getInstance().getLoggerProvider().emptyLine();
                        commandSender.sendMessage(
                            "The following cloud-servers are started on \"" + e.getName() + "\": ");
                        ReformCloudController.getInstance().getInternalCloudNetwork()
                            .getServerProcessManager().getAllRegisteredServerProcesses().stream()
                            .filter(serverInfo -> serverInfo.getCloudProcess().getClient()
                                .equals(e.getName())).forEach(info -> commandSender.sendMessage(
                            "    - " + info.getCloudProcess().getName() + " | State=" + info
                                .getServerState() + " | Player=" + info.getOnline() + "/" + info
                                .getServerGroup().getMaxPlayers()));
                        ReformCloudController.getInstance().getLoggerProvider().emptyLine();
                    });
                } else {
                    switch (args[1].toLowerCase()) {
                        case "server": {
                            List<ServerInfo> connected = new ArrayList<>();
                            ReformCloudController.getInstance()
                                .getInternalCloudNetwork()
                                .getServerProcessManager()
                                .getAllRegisteredServerProcesses()
                                .stream()
                                .filter(e -> e.getServerGroup().getName().equalsIgnoreCase(args[2]))
                                .forEach(connected::add);
                            commandSender.sendMessage(
                                "The following servers of the group \"" + args[2]
                                    + "\" are connected");
                            connected.forEach(info -> commandSender.sendMessage(
                                "    - " + info.getCloudProcess().getName() + " | Player=" + info
                                    .getOnline() + "/" + info.getServerGroup().getMaxPlayers()));
                            break;
                        }
                        case "proxy": {
                            List<ProxyInfo> connected = new ArrayList<>();
                            ReformCloudController.getInstance()
                                .getInternalCloudNetwork()
                                .getServerProcessManager()
                                .getAllRegisteredProxyProcesses()
                                .stream()
                                .filter(e -> e.getProxyGroup().getName().equalsIgnoreCase(args[2]))
                                .forEach(connected::add);
                            commandSender.sendMessage(
                                "The following proxies of the group \"" + args[2]
                                    + "\" are connected");
                            connected.forEach(info -> commandSender.sendMessage(
                                "    - " + info.getCloudProcess().getName() + " | Player=" + info
                                    .getOnline() + "/" + info.getProxyGroup().getMaxPlayers()));
                            break;
                        }
                        default: {
                            commandSender.sendMessage("process list <server/proxy> <name>");
                            break;
                        }
                    }
                }
                break;
            }
            case "start": {
                if (args.length == 2) {
                    if (ReformCloudController.getInstance().getInternalCloudNetwork()
                        .getServerGroups().containsKey(args[1])) {
                        final ServerGroup serverGroup = ReformCloudController.getInstance()
                            .getInternalCloudNetwork().getServerGroups().get(args[1]);
                        final Client client = ReformCloudController.getInstance()
                            .getBestClient(serverGroup.getClients(), serverGroup.getMemory());

                        if (client != null) {
                            final String id = Integer.toString(
                                ReformCloudController.getInstance().getCloudProcessOfferService()
                                    .nextServerID(serverGroup.getName()));
                            final String name =
                                serverGroup.getName() + ReformCloudController.getInstance()
                                    .getCloudConfiguration().getSplitter() + (
                                    Integer.parseInt(id) <= 9 ? "0" : "") + id;
                            ReformCloudController.getInstance().getCloudProcessOfferService()
                                .registerID(serverGroup.getName(), name, Integer.valueOf(id));
                            ReformCloudController.getInstance().getChannelHandler()
                                .sendPacketAsynchronous(client.getName(),
                                    new PacketOutStartGameServer(serverGroup, name,
                                        UUID.randomUUID(), new Configuration(), id)
                                );
                            commandSender.sendMessage(language.getCommand_process_trying_startup());
                            ReformCloudLibraryService.sleep(100);
                        } else {
                            commandSender
                                .sendMessage(language.getNo_available_client_for_startup());
                        }
                    } else if (ReformCloudController.getInstance().getInternalCloudNetwork()
                        .getProxyGroups().containsKey(args[1])) {
                        final ProxyGroup proxyGroup = ReformCloudController.getInstance()
                            .getInternalCloudNetwork().getProxyGroups().get(args[1]);
                        final Client client = ReformCloudController.getInstance()
                            .getBestClient(proxyGroup.getClients(), proxyGroup.getMemory());

                        if (client != null) {
                            final String id = Integer.toString(
                                ReformCloudController.getInstance().getCloudProcessOfferService()
                                    .nextProxyID(proxyGroup.getName()));
                            final String name =
                                proxyGroup.getName() + ReformCloudController.getInstance()
                                    .getCloudConfiguration().getSplitter() + (
                                    Integer.parseInt(id) <= 9 ? "0" : "") + id;
                            ReformCloudController.getInstance().getCloudProcessOfferService()
                                .registerProxyID(proxyGroup.getName(), name, Integer.valueOf(id));
                            ReformCloudController.getInstance().getChannelHandler()
                                .sendPacketAsynchronous(client.getName(),
                                    new PacketOutStartProxy(proxyGroup, name, UUID.randomUUID(),
                                        new Configuration(), id)
                                );
                            commandSender.sendMessage(language.getCommand_process_trying_startup());
                            ReformCloudLibraryService.sleep(100);
                        } else {
                            commandSender
                                .sendMessage(language.getNo_available_client_for_startup());
                        }
                    } else {
                        commandSender.sendMessage(
                            ReformCloudController.getInstance().getLoadedLanguage()
                                .getCommand_error_occurred()
                                .replace("%message%",
                                    "This Server or Proxy is not connected to controller"));
                    }
                } else {
                    commandSender.sendMessage("process start <groupName>");
                }
                break;
            }
            case "restart": {
                if (args.length == 2) {
                    ServerInfo serverInfo = ReformCloudController.getInstance()
                        .getServerInfo(args[1]);
                    if (serverInfo != null) {
                        ReformCloudController.getInstance().getChannelHandler()
                            .sendPacketAsynchronous(serverInfo.getCloudProcess().getClient(),
                                new PacketOutStopProcess(serverInfo.getCloudProcess().getName()));
                        commandSender.sendMessage(language.getCommand_process_try_stop()
                            .replace("%name%", serverInfo.getCloudProcess().getName()));
                        ReformCloudLibraryService.sleep(TimeUnit.MILLISECONDS, 100);
                        final ServerGroup serverGroup = serverInfo.getServerGroup();
                        final Client client = ReformCloudController.getInstance()
                            .getBestClient(serverGroup.getClients(), serverGroup.getMemory());

                        if (client != null) {
                            final String id = Integer.toString(
                                ReformCloudController.getInstance().getCloudProcessOfferService()
                                    .nextServerID(serverGroup.getName()));
                            final String name =
                                serverGroup.getName() + ReformCloudController.getInstance()
                                    .getCloudConfiguration().getSplitter() + (
                                    Integer.parseInt(id) <= 9 ? "0" : "") + id;
                            ReformCloudController.getInstance().getCloudProcessOfferService()
                                .registerID(serverGroup.getName(), name, Integer.valueOf(id));
                            ReformCloudController.getInstance().getChannelHandler()
                                .sendPacketAsynchronous(client.getName(),
                                    new PacketOutStartGameServer(serverGroup, name,
                                        UUID.randomUUID(), new Configuration(), id)
                                );
                            commandSender.sendMessage(language.getCommand_process_trying_startup());
                            ReformCloudLibraryService.sleep(100);
                        } else {
                            commandSender
                                .sendMessage(language.getNo_available_client_for_startup());
                        }
                    } else if (ReformCloudController.getInstance().getProxyInfo(args[1]) != null) {
                        ProxyInfo proxyInfo = ReformCloudController.getInstance()
                            .getProxyInfo(args[1]);
                        ReformCloudController.getInstance().getChannelHandler()
                            .sendPacketAsynchronous(proxyInfo.getCloudProcess().getClient(),
                                new PacketOutStopProcess(proxyInfo.getCloudProcess().getName()));
                        commandSender.sendMessage(language.getCommand_process_try_stop()
                            .replace("%name%", serverInfo.getCloudProcess().getName()));
                        ReformCloudLibraryService.sleep(100);
                        final ProxyGroup proxyGroup = proxyInfo.getProxyGroup();
                        final Client client = ReformCloudController.getInstance()
                            .getBestClient(proxyGroup.getClients(), proxyGroup.getMemory());

                        if (client != null) {
                            final String id = Integer.toString(
                                ReformCloudController.getInstance().getCloudProcessOfferService()
                                    .nextProxyID(proxyGroup.getName()));
                            final String name =
                                proxyGroup.getName() + ReformCloudController.getInstance()
                                    .getCloudConfiguration().getSplitter() + (
                                    Integer.parseInt(id) <= 9 ? "0" : "") + id;
                            ReformCloudController.getInstance().getCloudProcessOfferService()
                                .registerProxyID(proxyGroup.getName(), name, Integer.valueOf(id));
                            ReformCloudController.getInstance().getChannelHandler()
                                .sendPacketAsynchronous(client.getName(),
                                    new PacketOutStartProxy(proxyGroup, name, UUID.randomUUID(),
                                        new Configuration(), id)
                                );
                            commandSender.sendMessage(language.getCommand_process_trying_startup());
                            ReformCloudLibraryService.sleep(100);
                        } else {
                            commandSender
                                .sendMessage(language.getNo_available_client_for_startup());
                        }
                    } else {
                        commandSender.sendMessage(
                            ReformCloudController.getInstance().getLoadedLanguage()
                                .getCommand_error_occurred()
                                .replace("%message%",
                                    "This Server or Proxy is not connected to controller"));
                    }
                } else {
                    commandSender.sendMessage("process restart <name>");
                }
                break;
            }
            default: {
                commandSender.sendMessage("process stop <name/--all/--empty>");
                commandSender.sendMessage("process restart <name>");
                commandSender.sendMessage("process stopGroup <group>");
                commandSender.sendMessage("process start <group-name> <number>");
                commandSender.sendMessage("process list");
                commandSender.sendMessage("process list <server/proxy> <name>");
                commandSender.sendMessage("");
                commandSender.sendMessage("process queue <client> list");
                commandSender.sendMessage("process queue <client> remove <proxy/server> <name>");
            }
        }
    }
}
