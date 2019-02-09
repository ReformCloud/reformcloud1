/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.netty.out.*;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public final class CommandProcess extends Command implements Serializable {
    private final DecimalFormat decimalFormat = new DecimalFormat("##.###");

    public CommandProcess() {
        super("process", "Main command for all processes", "reformcloud.command.process", new String[0]);
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length < 1) {
            commandSender.sendMessage("process stop <name/--all/--empty>");
            commandSender.sendMessage("process start <group-name>");
            commandSender.sendMessage("process list");
            commandSender.sendMessage("process list <server/proxy> <name>");
            commandSender.sendMessage("");
            commandSender.sendMessage("process queue <client> list");
            commandSender.sendMessage("process queue <client> remove <proxy/server> <name>");
            return;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("queue") && args[2].equalsIgnoreCase("list")) {
            Client client = ReformCloudController.getInstance().getInternalCloudNetwork().getClients().get(args[1]);
            if (client == null || client.getClientInfo() == null) {
                commandSender.sendMessage("Client isn't registered in CloudController");
                return;
            }

            ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(client.getName(), new PacketOutGetClientProcessQueue());
            commandSender.sendMessage("The queue was requested successfully");
            return;
        } else if (args.length == 5
                && args[0].equalsIgnoreCase("queue")
                && args[2].equalsIgnoreCase("remove")) {
            Client client = ReformCloudController.getInstance().getInternalCloudNetwork().getClients().get(args[1]);
            if (client == null || client.getClientInfo() == null) {
                commandSender.sendMessage("Client isn't registered in CloudController");
                return;
            }

            switch (args[3].toLowerCase()) {
                case "proxy": {
                    ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(
                            client.getName(), new PacketOutRemoveProxyQueueProcess(args[4])
                    );
                    commandSender.sendMessage("Trying to §cremove§r queue entry §e" + args[4]);
                    break;
                }

                case "server": {
                    ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(
                            client.getName(), new PacketOutRemoveServerQueueProcess(args[4])
                    );
                    commandSender.sendMessage("Trying to §cremove§r queue entry §e" + args[4]);
                    break;
                }

                default: {
                    commandSender.sendMessage("process stop <name/--all/--empty>");
                    commandSender.sendMessage("process start <group-name>");
                    commandSender.sendMessage("process list");
                    commandSender.sendMessage("process list <server/proxy> <name>");
                    commandSender.sendMessage("");
                    commandSender.sendMessage("process queue <client> list");
                    commandSender.sendMessage("process queue <client> remove <proxy/server> <name>");
                }
            }

            return;
        }

        switch (args[0]) {
            case "stop": {
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("--all")) {
                        ReformCloudController.getInstance()
                                .getInternalCloudNetwork()
                                .getServerProcessManager()
                                .getAllRegisteredServerProcesses()
                                .forEach(info -> {
                                    ReformCloudController.getInstance()
                                            .getChannelHandler()
                                            .sendPacketAsynchronous(info.getCloudProcess().getClient(),
                                                    new PacketOutStopProcess(info.getCloudProcess().getName()));
                                    commandSender.sendMessage("Trying to stop " + info.getCloudProcess().getName() + "...");
                                    ReformCloudLibraryService.sleep(25);
                                });
                        ReformCloudController.getInstance()
                                .getInternalCloudNetwork()
                                .getServerProcessManager()
                                .getAllRegisteredProxyProcesses()
                                .forEach(info -> {
                                    ReformCloudController.getInstance()
                                            .getChannelHandler()
                                            .sendPacketAsynchronous(info.getCloudProcess().getClient(),
                                                    new PacketOutStopProcess(info.getCloudProcess().getName()));
                                    commandSender.sendMessage("Trying to stop " + info.getCloudProcess().getName() + "...");
                                    ReformCloudLibraryService.sleep(25);
                                });
                        return;
                    }

                    if (args[1].equalsIgnoreCase("--empty")) {
                        ReformCloudController.getInstance()
                                .getInternalCloudNetwork()
                                .getServerProcessManager()
                                .getAllRegisteredServerProcesses()
                                .stream()
                                .filter(info -> info.getOnline() == 0)
                                .forEach(info -> {
                                    ReformCloudController.getInstance()
                                            .getChannelHandler()
                                            .sendPacketAsynchronous(info.getCloudProcess().getClient(),
                                                    new PacketOutStopProcess(info.getCloudProcess().getName()));
                                    commandSender.sendMessage("Trying to stop " + info.getCloudProcess().getName() + "...");
                                    ReformCloudLibraryService.sleep(25);
                                });
                        ReformCloudController.getInstance()
                                .getInternalCloudNetwork()
                                .getServerProcessManager()
                                .getAllRegisteredProxyProcesses()
                                .stream()
                                .filter(info -> info.getOnline() == 0)
                                .forEach(info -> {
                                    ReformCloudController.getInstance()
                                            .getChannelHandler()
                                            .sendPacketAsynchronous(info.getCloudProcess().getClient(),
                                                    new PacketOutStopProcess(info.getCloudProcess().getName()));
                                    commandSender.sendMessage("Trying to stop " + info.getCloudProcess().getName() + "...");
                                    ReformCloudLibraryService.sleep(25);
                                });
                    }

                    if (ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredProxyByName(args[1]) != null) {
                        final ProxyInfo proxyInfo = ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredProxyByName(args[1]);
                        ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(proxyInfo.getCloudProcess().getClient(), new PacketOutStopProcess(proxyInfo.getCloudProcess().getName()));
                        commandSender.sendMessage("Trying to stop " + proxyInfo.getCloudProcess().getName() + "...");
                    } else if (ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredServerByName(args[1]) != null) {
                        final ServerInfo serverInfo = ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredServerByName(args[1]);
                        ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(serverInfo.getCloudProcess().getClient(), new PacketOutStopProcess(serverInfo.getCloudProcess().getName()));
                        commandSender.sendMessage("Trying to stop " + serverInfo.getCloudProcess().getName() + "...");
                    } else
                        commandSender.sendMessage("This Server or Proxy is not connected to controller");
                    break;
                } else {
                    commandSender.sendMessage("process stop <name>");
                }
            }
            case "list": {
                if (args.length != 3) {
                    List<Client> connectedClients = new ArrayList<>();
                    ReformCloudController.getInstance().getInternalCloudNetwork()
                            .getClients()
                            .values()
                            .stream()
                            .filter(client -> client.getClientInfo() != null)
                            .forEach(client -> connectedClients.add(client));

                    commandSender.sendMessage("The following Clients are connected: ");
                    connectedClients.forEach(e -> {
                        commandSender.sendMessage("    - " + e.getName() + " | Host=" + e.getIp() + " | Memory-Usage=" + e.getClientInfo().getUsedMemory() + "MB/" + e.getClientInfo().getMaxMemory() + "MB | Processors: " + e.getClientInfo().getCpuCoresSystem() + " | CPU-Usage: " + decimalFormat.format(e.getClientInfo().getCpuUsage()) + "% | Started-Processes: " + (e.getClientInfo().getStartedProxies().size() + e.getClientInfo().getStartedServers().size()));
                        ReformCloudController.getInstance().getLoggerProvider().emptyLine();
                        commandSender.sendMessage("The following proxies are started on \"" + e.getName() + "\": ");
                        ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getAllRegisteredProxyProcesses().stream().filter(proxyInfo -> proxyInfo.getCloudProcess().getClient().equals(e.getName())).forEach(info -> {
                            commandSender.sendMessage("    - " + info.getCloudProcess().getName() + " | Player=" + info.getOnline() + "/" + info.getProxyGroup().getMaxPlayers());
                        });
                        ReformCloudController.getInstance().getLoggerProvider().emptyLine();
                        commandSender.sendMessage("The following cloud-servers are started on \"" + e.getName() + "\": ");
                        ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getAllRegisteredServerProcesses().stream().filter(serverInfo -> serverInfo.getCloudProcess().getClient().equals(e.getName())).forEach(info -> {
                            commandSender.sendMessage("    - " + info.getCloudProcess().getName() + " | Player=" + info.getOnline() + "/" + info.getServerGroup().getMaxPlayers());
                        });
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
                                    .stream().filter(e -> e.getServerGroup().getName().equalsIgnoreCase(args[2]))
                                    .forEach(e -> connected.add(e));
                            commandSender.sendMessage("The following servers of the group \"" + args[2] + "\" are connected");
                            connected.forEach(info -> commandSender.sendMessage("    - " + info.getCloudProcess().getName() + " | Player=" + info.getOnline() + "/" + info.getServerGroup().getMaxPlayers()));
                            break;
                        }
                        case "proxy": {
                            List<ProxyInfo> connected = new ArrayList<>();
                            ReformCloudController.getInstance()
                                    .getInternalCloudNetwork()
                                    .getServerProcessManager()
                                    .getAllRegisteredProxyProcesses()
                                    .stream().filter(e -> e.getProxyGroup().getName().equalsIgnoreCase(args[2]))
                                    .forEach(e -> connected.add(e));
                            commandSender.sendMessage("The following proxies of the group \"" + args[2] + "\" are connected");
                            connected.forEach(info -> commandSender.sendMessage("    - " + info.getCloudProcess().getName() + " | Player=" + info.getOnline() + "/" + info.getProxyGroup().getMaxPlayers()));
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
                    if (ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups().containsKey(args[1])) {
                        final ServerGroup serverGroup = ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups().get(args[1]);
                        final Client client = ReformCloudController.getInstance().getBestClient(serverGroup.getClients(), serverGroup.getMemory());

                        if (client != null) {
                            final String id = ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().nextFreeServerID(serverGroup.getName());
                            final String name = serverGroup.getName() + ReformCloudController.getInstance().getCloudConfiguration().getSplitter() + (Integer.parseInt(id) <= 9 ? "0" : "") + id;
                            ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(client.getName(),
                                    new PacketOutStartGameServer(serverGroup, name, UUID.randomUUID(), new Configuration(), id)
                            );
                            commandSender.sendMessage("Trying to startup serverProcess...");
                        } else {
                            commandSender.sendMessage("The Client of the ServerGroup isn't connected to ReformCloudController or Client is not available to startup processes");
                        }
                    } else if (ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().containsKey(args[1])) {
                        final ProxyGroup proxyGroup = ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().get(args[1]);
                        final Client client = ReformCloudController.getInstance().getBestClient(proxyGroup.getClients(), proxyGroup.getMemory());

                        if (client != null) {
                            final String id = ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().nextFreeProxyID(proxyGroup.getName());
                            final String name = proxyGroup.getName() + ReformCloudController.getInstance().getCloudConfiguration().getSplitter() + (Integer.parseInt(id) <= 9 ? "0" : "") + id;
                            ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(client.getName(),
                                    new PacketOutStartProxy(proxyGroup, name, UUID.randomUUID(), new Configuration(), id)
                            );
                            commandSender.sendMessage("Trying to startup proxyProcess...");
                        } else {
                            commandSender.sendMessage("The Client of the ProxyGroup isn't connected to ReformCloudController or Client is not available to startup processes");
                        }
                    } else {
                        commandSender.sendMessage("ServerGroup or ProxyGroup doesn't exists");
                    }
                } else {
                    commandSender.sendMessage("process start <groupName>");
                }
                break;
            }
            default: {
                commandSender.sendMessage("process stop <name/--all/--empty>");
                commandSender.sendMessage("process start <group-name>");
                commandSender.sendMessage("process list");
                commandSender.sendMessage("process list <server/proxy> <name>");
                commandSender.sendMessage("");
                commandSender.sendMessage("process queue <client> list");
                commandSender.sendMessage("process queue <client> remove <proxy/server> <name>");
            }
        }
    }
}
