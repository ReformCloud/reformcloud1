/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.optional.qual.MaybePresent;
import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.bootstrap.VelocityBootstrap;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.network.packets.PacketOutDispatchConsoleCommand;
import systems.reformcloud.utility.StringUtil;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public final class CommandReformCloud implements Command {

    @Override
    public void execute(@MaybePresent CommandSource commandSource,
        @NonNull @MaybePresent String[] strings) {
        if (!commandSource.hasPermission("reformcloud.command.reformcloud")) {
            commandSource.sendMessage(TextComponent
                .of(ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                    .getMessage("internal-api-bungee-command-no-permission")));
            return;
        }

        final String prefix = (ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getPrefix().endsWith(" ") ?
            ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getPrefix() :
            ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getPrefix() + " ") + "§7";

        if (strings.length == 0) {
            commandSource.sendMessage(TextComponent
                .of(ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                    .getMessage("internal-api-bungee-command-reformcloud-invalid-syntax")));
            sendHelp(commandSource, prefix);
            return;
        }

        if (!commandSource.hasPermission("reformcloud.command." + strings[0])) {
            commandSource.sendMessage(TextComponent
                .of(ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                    .getMessage("internal-api-bungee-command-reformcloud-no-permission")));
            return;
        }

        if (strings[0].equalsIgnoreCase("version")) {
            commandSource.sendMessage(
                TextComponent.of("You are using the ReformCloud V" + StringUtil.REFORM_VERSION));
            return;
        }

        if (strings[0].equalsIgnoreCase("maintenance")) {
            if (strings.length != 2) {
                ProxyGroup proxyGroup =
                    ReformCloudAPIVelocity.getInstance().getProxyInfo().getProxyGroup();
                ReformCloudAPIVelocity.getInstance().dispatchConsoleCommand(
                    "asg proxygroup " + proxyGroup.getName() +
                        " maintenance " + (proxyGroup.isMaintenance() ? "false --update" :
                        "true --update")
                );
                commandSource.sendMessage(TextComponent.of(
                    ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                        .getMessage("internal-api-bungee-command-send-controller")
                ));
                return;
            }
            ServerGroup serverGroup =
                ReformCloudAPIVelocity.getInstance().getServerGroup(strings[1]);
            if (serverGroup != null) {
                ReformCloudAPIVelocity.getInstance().dispatchConsoleCommand(
                    "asg servergroup " + serverGroup.getName() +
                        " maintenance " + (serverGroup.isMaintenance() ? "false" : "true") +
                        " --update"
                );
                commandSource.sendMessage(TextComponent.of(
                    ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                        .getMessage("internal-api-bungee-command-send-controller")
                ));
                return;
            }
            commandSource.sendMessage(TextComponent.of(prefix + "§cThe servergroup doesn't exists"));
            return;
        }

        if (strings[0].equalsIgnoreCase("copy")) {
            if (strings.length == 2) {
                ReformCloudAPIVelocity.getInstance().getChannelHandler()
                    .sendPacketAsynchronous("ReformCloudController",
                        new PacketOutDispatchConsoleCommand("copy " + strings[1]));
                commandSource.sendMessage(TextComponent.of(
                    ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                        .getMessage("internal-api-bungee-command-send-controller")
                ));
            } else if (strings.length == 3) {
                ReformCloudAPIVelocity.getInstance().getChannelHandler()
                    .sendPacketAsynchronous("ReformCloudController",
                        new PacketOutDispatchConsoleCommand(
                            "copy " + strings[1] + " " + strings[2]));
                commandSource.sendMessage(TextComponent.of(
                    ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                        .getMessage("internal-api-bungee-command-send-controller")
                ));
            } else {
                commandSource
                    .sendMessage(TextComponent.of(prefix + "/reformcloud copy <name> <file/dir>"));
            }

            return;
        }

        if (strings[0].equalsIgnoreCase("whitelist")) {
            if (strings.length == 4) {
                StringBuilder stringBuilder = new StringBuilder();
                Arrays.stream(strings)
                    .forEach(e -> stringBuilder.append(e).append(StringUtil.SPACE));

                ReformCloudAPIVelocity.getInstance().getChannelHandler()
                    .sendPacketAsynchronous("ReformCloudController",
                        new PacketOutDispatchConsoleCommand(
                            stringBuilder.substring(0, stringBuilder.length() - 1)));
                commandSource.sendMessage(TextComponent.of(
                    ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                        .getMessage("internal-api-bungee-command-send-controller")
                ));
            } else {
                commandSource.sendMessage(TextComponent
                    .of("/reformcloud whitelist <add/remove> <proxyGroup/--all> <name>"));
            }

            return;
        }

        if (strings[0].equalsIgnoreCase("execute")) {
            if (strings.length > 3) {
                StringBuilder stringBuilder = new StringBuilder();
                Arrays.stream(strings)
                    .forEach(e -> stringBuilder.append(e).append(StringUtil.SPACE));

                ReformCloudAPIVelocity.getInstance().getChannelHandler()
                    .sendPacketAsynchronous("ReformCloudController",
                        new PacketOutDispatchConsoleCommand(
                            stringBuilder.substring(0, stringBuilder.length() - 1)));
                commandSource.sendMessage(TextComponent.of(
                    ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                        .getMessage("internal-api-bungee-command-send-controller")
                ));
            } else {
                commandSource.sendMessage(
                    TextComponent.of("/reformcloud execute <server/proxy> <name> <command>"));
            }

            return;
        }

        if (strings[0].equalsIgnoreCase("process")) {
            if (strings.length == 3) {
                StringBuilder stringBuilder = new StringBuilder();
                Arrays.stream(strings)
                    .forEach(e -> stringBuilder.append(e).append(StringUtil.SPACE));

                ReformCloudAPIVelocity.getInstance().getChannelHandler()
                    .sendPacketAsynchronous("ReformCloudController",
                        new PacketOutDispatchConsoleCommand(
                            stringBuilder.substring(0, stringBuilder.length() - 1)));
                commandSource.sendMessage(TextComponent.of(
                    ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                        .getMessage("internal-api-bungee-command-send-controller")
                ));
            } else {
                commandSource.sendMessage(
                    TextComponent.of("/reformcloud process <start/stop> <group/name>"));
            }

            return;
        }

        if (strings[0].equalsIgnoreCase("reload")) {
            ReformCloudAPIVelocity.getInstance().getChannelHandler()
                .sendPacketAsynchronous("ReformCloudController",
                    new PacketOutDispatchConsoleCommand("reload"));
            commandSource.sendMessage(TextComponent.of(
                ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                    .getMessage("internal-api-bungee-command-send-controller")
            ));
            return;
        }

        if (strings[0].equalsIgnoreCase("list")) {
            if (strings.length != 3) {
                List<Client> connectedClients = new ArrayList<>();
                ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                    .getClients()
                    .values()
                    .stream()
                    .filter(client -> client.getClientInfo() != null)
                    .forEach(connectedClients::add);

                if (connectedClients.size() > 0) {
                    commandSource.sendMessage(TextComponent.of(prefix + "The following §eClients§7 are connected: "));
                    connectedClients.forEach(e -> {
                        final ClientInfo clientInfo = ReformCloudAPIVelocity.getInstance().getConnectedClient(e.getName());
                        commandSource.sendMessage(TextComponent.of(
                            "    - §e" + e.getName() + "§7 | Host: §e" + e.getIp() + "§7 | Memory-Usage: §e"
                                + getMemoryOf(e.getName()) + "MB§7/§e" + clientInfo.getMaxMemory()
                                + "MB§7 | Processors: §e" + clientInfo.getCpuCoresSystem()
                                + "§7 | CPU-Usage: §e" + new DecimalFormat("##.###").format(clientInfo.getCpuUsage())
                                + "%§7 | Started-Processes: §e" + (clientInfo.getStartedProxies().size()
                                + clientInfo.getStartedServers().size())));
                        commandSource.sendMessage(TextComponent.of(""));
                    });

                    connectedClients.forEach(e -> {
                        if (ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                            .getServerProcessManager().getAllRegisteredProxyProcesses().stream()
                            .anyMatch(proxies -> proxies.getCloudProcess().getClient()
                                .equals(e.getName()))) {
                            commandSource.sendMessage(TextComponent.of(
                                prefix + "§7The following §eproxies§7 are started on \"§e" + e.getName() + "§7\": "));
                            ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                                .getServerProcessManager().getAllRegisteredProxyProcesses().stream()
                                .filter(proxyInfo -> proxyInfo.getCloudProcess().getClient()
                                    .equals(e.getName())).forEach(info -> commandSource.sendMessage(TextComponent.of(
                                "    - §e" + info.getCloudProcess().getName() + "§7 | Maintenance: §" + info.getProxyGroup().isMaintenance() + "§7 | Player: §e" + info
                                    .getOnline() + "§7/§e" + info.getProxyGroup().getMaxPlayers())));
                            commandSource.sendMessage(TextComponent.of(""));
                        } else {
                            commandSource.sendMessage(TextComponent.of(prefix + "There are no started §eproxies§7 on \"§e" + e.getName() + "§7\""));
                            commandSource.sendMessage(TextComponent.of(""));
                        }

                        if (ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                            .getServerProcessManager().getAllRegisteredServerProcesses().stream()
                            .anyMatch(servers -> servers.getCloudProcess().getClient()
                                .equals(e.getName()))) {
                            commandSource.sendMessage(TextComponent.of(
                                prefix + "The following §eservers§7 are started on \"§e" + e.getName() + "§7\": "));
                            ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                                .getServerProcessManager().getAllRegisteredServerProcesses().stream()
                                .filter(serverInfo -> serverInfo.getCloudProcess().getClient()
                                    .equals(e.getName())).forEach(info -> commandSource.sendMessage(TextComponent.of(
                                "    - §e" + info.getCloudProcess().getName() + "§7 | State: §e" + info
                                    .getServerState() + "§7 | Player: §e" + info.getOnline() + "§7/§e" + info
                                    .getServerGroup().getMaxPlayers())));
                            commandSource.sendMessage(TextComponent.of(""));
                        } else {
                            commandSource.sendMessage(TextComponent.of(prefix + "There are no started §eservers§7 on \"§e" + e.getName() + "$7\""));
                            commandSource.sendMessage(TextComponent.of(""));
                        }
                    });
                } else
                    commandSource.sendMessage(TextComponent.of(prefix + "There are no §eClients§7 connected to the §eController"));
            } else {
                switch (strings[1].toLowerCase()) {
                    case "server": {
                        if (ReformCloudAPIVelocity.getInstance().getServerGroup(strings[2]) != null) {
                            List<ServerInfo> connected = new ArrayList<>();
                            ReformCloudAPIVelocity.getInstance()
                                .getInternalCloudNetwork()
                                .getServerProcessManager()
                                .getAllRegisteredServerProcesses()
                                .stream()
                                .filter(e -> e.getServerGroup().getName().equalsIgnoreCase(strings[2]))
                                .forEach(connected::add);

                            if (connected.size() > 0) {
                                commandSource.sendMessage(TextComponent.of(
                                    prefix + "The following §eservers§7 of the group \"§e" + strings[2]
                                        + "§7\" are connected: "));
                                connected.forEach(info -> commandSource.sendMessage(TextComponent.of(
                                    "    - §e" + info.getCloudProcess().getName() + "§7 | Player: §e" + info
                                        .getOnline() + "§7/§e" + info.getServerGroup().getMaxPlayers())));
                            } else
                                commandSource.sendMessage(TextComponent.of(prefix + "There are no started §eservers§7 of the group \"§e" + strings[2] + "§7\""));
                        } else
                            commandSource.sendMessage(TextComponent.of(prefix + "§cThe servergroup doesn't exists"));

                        break;
                    }
                    case "proxy": {
                        if (ReformCloudAPIVelocity.getInstance().getProxyGroup(strings[2]) != null) {
                            List<ProxyInfo> connected = new ArrayList<>();
                            ReformCloudAPIVelocity.getInstance()
                                .getInternalCloudNetwork()
                                .getServerProcessManager()
                                .getAllRegisteredProxyProcesses()
                                .stream()
                                .filter(e -> e.getProxyGroup().getName().equalsIgnoreCase(strings[2]))
                                .forEach(connected::add);

                            if (connected.size() > 0) {
                                commandSource.sendMessage(TextComponent.of(
                                    prefix + "The following §eproxies§7 of the group \"§e" + strings[2]
                                        + "§7\" are connected: "));
                                connected.forEach(info -> commandSource.sendMessage(TextComponent.of(
                                    "    - §e" + info.getCloudProcess().getName() + "§7 | Maintenance: §e" + info.getProxyGroup().isMaintenance() + "§7 | Player: §e" + info
                                        .getOnline() + "§7/§e" + info.getProxyGroup().getMaxPlayers())));
                            } else
                                commandSource.sendMessage(TextComponent.of(prefix + "There are no started §eproxies§7 of the group \"§e" + strings[2] + "§7\""));
                        } else
                            commandSource.sendMessage(TextComponent.of(prefix + "§cThe proxygroup doesn't exists"));

                        break;
                    }
                    default: {
                        commandSource.sendMessage(TextComponent.of(prefix + "process list <server/proxy> <group-name>"));
                        break;
                    }
                }
            }
            return;
        }

        commandSource.sendMessage(TextComponent
            .of(ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                .getMessage("internal-api-bungee-command-reformcloud-invalid-syntax")));
        sendHelp(commandSource, prefix);
    }

    @Override
    public boolean hasPermission(CommandSource source, @NonNull String[] args) {
        return source.getPermissionValue("reformcloud.command.reformcloud").asBoolean();
    }

    @Override
    public @MaybePresent List<String> suggest(@MaybePresent CommandSource source,
        @NonNull @MaybePresent String[] strings) {
        if (!source.hasPermission("reformcloud.command.reformcloud")) {
            return new LinkedList<>();
        }

        if (strings.length == 2 && strings[0].equalsIgnoreCase("maintenance")) {
            return registeredServerGroups();
        }

        if (strings.length == 2 && strings[0].equalsIgnoreCase("copy")) {
            return registered();
        }

        if (strings.length == 2 && strings[0].equalsIgnoreCase("whitelist")) {
            return Arrays.asList("add", "remove");
        }

        if (strings.length == 3 && strings[0].equalsIgnoreCase("whitelist")) {
            List<String> out = registeredProxyGroups();
            out.add("--all");
            return out;
        }

        if (strings.length == 4 && strings[0].equalsIgnoreCase("whitelist")) {
            return players();
        }

        if (strings.length == 2 && strings[0].equalsIgnoreCase("execute")) {
            return Arrays.asList("server", "proxy");
        }

        if (strings.length == 3 && strings[0].equalsIgnoreCase("execute")) {
            if (strings[1].equalsIgnoreCase("server")) {
                return registeredServers();
            }

            if (strings[1].equalsIgnoreCase("proxy")) {
                return registeredProxies();
            }

            return registered();
        }

        if (strings.length == 4 && strings[0].equalsIgnoreCase("execute")) {
            return Arrays.asList("ban", "help", "reformcloud");
        }

        if (strings.length == 2 && strings[0].equalsIgnoreCase("process")) {
            return Arrays.asList("start", "stop");
        }

        if (strings.length == 3 && strings[0].equalsIgnoreCase("process")) {
            return registeredGroups();
        }

        if (strings.length == 2 && strings[0].equalsIgnoreCase("list")) {
            return Arrays.asList("server", "proxy");
        }

        if (strings.length == 3 && strings[0].equalsIgnoreCase("list")) {
            if (strings[1].equalsIgnoreCase("server")) {
                return registeredServerGroups();
            }

            if (strings[1].equalsIgnoreCase("proxy")) {
                return registeredProxyGroups();
            }

            return registeredGroups();
        }

        return Arrays.asList("copy", "maintenance", "whitelist", "execute",
            "process", "list", "reload", "version");
    }

    private List<String> registered() {
        List<String> out = new LinkedList<>();
        ReformCloudAPIVelocity.getInstance().getAllRegisteredProxies()
            .forEach(e -> out.add(e.getCloudProcess().getName()));
        ReformCloudAPIVelocity.getInstance().getAllRegisteredServers()
            .forEach(e -> out.add(e.getCloudProcess().getName()));
        return out;
    }

    private List<String> registeredServers() {
        List<String> out = new LinkedList<>();
        ReformCloudAPIVelocity.getInstance().getAllRegisteredServers()
            .forEach(e -> out.add(e.getCloudProcess().getName()));
        return out;
    }

    private List<String> registeredProxies() {
        List<String> out = new LinkedList<>();
        ReformCloudAPIVelocity.getInstance().getAllRegisteredProxies()
            .forEach(e -> out.add(e.getCloudProcess().getName()));
        return out;
    }

    private List<String> registeredGroups() {
        List<String> out = new LinkedList<>();
        ReformCloudAPIVelocity.getInstance().getAllProxyGroups().forEach(e -> out.add(e.getName()));
        ReformCloudAPIVelocity.getInstance().getAllServerGroups()
            .forEach(e -> out.add(e.getName()));
        return out;
    }

    private List<String> registeredServerGroups() {
        List<String> out = new LinkedList<>();
        ReformCloudAPIVelocity.getInstance().getAllServerGroups().forEach(e -> out.add(e.getName()));
        return out;
    }

    private List<String> registeredProxyGroups() {
        List<String> out = new LinkedList<>();
        ReformCloudAPIVelocity.getInstance().getAllProxyGroups().forEach(e -> out.add(e.getName()));
        return out;
    }

    private List<String> players() {
        List<String> out = new LinkedList<>();
        VelocityBootstrap.getInstance().getProxy().getAllPlayers()
            .forEach(e -> out.add(e.getUsername()));
        return out;
    }

    private void sendHelp(CommandSource commandSource, String prefix) {
        commandSource.sendMessage(TextComponent.of(prefix + "§7/reformcloud copy <name> <file/dir> \n"));
        commandSource.sendMessage(TextComponent.of(prefix + "§7/reformcloud maintenance <serverGroup> \n"));
        commandSource.sendMessage(TextComponent.of(prefix + "§7/reformcloud whitelist <add/remove> <proxyGroup/--all> <name> \n"));
        commandSource.sendMessage(TextComponent.of(prefix + "§7/reformcloud execute <server/proxy> <name> <command> \n"));
        commandSource.sendMessage(TextComponent.of(prefix + "§7/reformcloud process <start/stop> <group/name> \n"));
        commandSource.sendMessage(TextComponent.of(prefix + "§7/reformcloud list <server/proxy> <group-name> \n"));
        commandSource.sendMessage(TextComponent.of(prefix + "§7/reformcloud reload \n"));
        commandSource.sendMessage(TextComponent.of(prefix + "§7/reformcloud version"));
    }

    private long getMemoryOf(String client) {
        AtomicLong atomicLong = new AtomicLong(0);
        ReformCloudAPIVelocity.getInstance().getAllRegisteredProxies()
            .stream()
            .filter(e -> e.getCloudProcess().getClient().equals(client))
            .forEach(e -> atomicLong.addAndGet(e.getMaxMemory()));
        ReformCloudAPIVelocity.getInstance().getAllRegisteredServers()
            .stream()
            .filter(e -> e.getCloudProcess().getClient().equals(client))
            .forEach(e -> atomicLong.addAndGet(e.getMaxMemory()));

        return atomicLong.get();
    }
}
