/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.launcher.BungeecordBootstrap;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.network.packets.PacketOutDispatchConsoleCommand;
import systems.reformcloud.utility.StringUtil;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public final class CommandReformCloud extends Command implements Serializable, TabExecutor {

    public CommandReformCloud() {
        super("reformcloud");
    }

    @Override
    public final String getPermission() {
        return "reformcloud.command.reformcloud";
    }

    @Override
    public final String[] getAliases() {
        return new String[]{"rc"};
    }

    @Override
    public void execute(final CommandSender commandSender, final String[] strings) {
        if (!commandSender.hasPermission("reformcloud.command.reformcloud")) {
            commandSender.sendMessage(TextComponent.fromLegacyText(
                ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                    .getMessage("internal-api-bungee-command-no-permission")));
            return;
        }

        final String prefix = (ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getPrefix().endsWith(" ") ?
                ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getPrefix() :
                ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getPrefix() + " ") + "§7";

        if (strings.length == 0) {
            commandSender.sendMessage(TextComponent.fromLegacyText(
                ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                    .getMessage("internal-api-bungee-command-reformcloud-invalid-syntax")));
            sendHelp(commandSender, prefix);
            return;
        }

        if (!commandSender.hasPermission("reformcloud.command." + strings[0])) {
            commandSender.sendMessage(TextComponent.fromLegacyText(
                ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                    .getMessage("internal-api-bungee-command-reformcloud-no-permission")));
            return;
        }

        if (strings[0].equalsIgnoreCase("version")) {
            commandSender.sendMessage(TextComponent
                .fromLegacyText(prefix + "§7You are using the §eReformCloud V" + StringUtil.REFORM_VERSION));
            return;
        }

        if (strings[0].equalsIgnoreCase("maintenance")) {
            ProxyGroup proxyGroup =
                ReformCloudAPIBungee.getInstance().getProxyInfo().getProxyGroup();
            ReformCloudAPIBungee.getInstance().dispatchConsoleCommand(
                "asg proxygroup " + proxyGroup.getName() +
                    " maintenance " + (proxyGroup.isMaintenance() ? "false --update" :
                    "true --update")
            );
            commandSender.sendMessage(TextComponent.fromLegacyText(
                ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                    .getMessage("internal-api-bungee-command-send-controller")
            ));
            return;
        }

        if (strings[0].equalsIgnoreCase("copy")) {
            if (strings.length == 2) {
                ReformCloudAPIBungee.getInstance().getChannelHandler()
                    .sendPacketAsynchronous("ReformCloudController",
                        new PacketOutDispatchConsoleCommand("copy " + strings[1]));
                commandSender.sendMessage(TextComponent.fromLegacyText(
                    ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                        .getMessage("internal-api-bungee-command-send-controller")
                ));
            } else if (strings.length == 3) {
                ReformCloudAPIBungee.getInstance().getChannelHandler()
                    .sendPacketAsynchronous("ReformCloudController",
                        new PacketOutDispatchConsoleCommand(
                            "copy " + strings[1] + " " + strings[2]));
                commandSender.sendMessage(TextComponent.fromLegacyText(
                    ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                        .getMessage("internal-api-bungee-command-send-controller")
                ));
            } else {
                commandSender
                    .sendMessage(TextComponent.fromLegacyText(prefix + "/reformcloud copy <name>"));
            }

            return;
        }

        if (strings[0].equalsIgnoreCase("whitelist")) {
            if (strings.length == 4) {
                StringBuilder stringBuilder = new StringBuilder();
                Arrays.stream(strings)
                    .forEach(e -> stringBuilder.append(e).append(StringUtil.SPACE));

                ReformCloudAPIBungee.getInstance().getChannelHandler()
                    .sendPacketAsynchronous("ReformCloudController",
                        new PacketOutDispatchConsoleCommand(
                            stringBuilder.substring(0, stringBuilder.length() - 1)));
                commandSender.sendMessage(TextComponent.fromLegacyText(
                    ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                        .getMessage("internal-api-bungee-command-send-controller")
                ));
            } else {
                commandSender.sendMessage(TextComponent.fromLegacyText(
                    prefix + "/reformcloud whitelist <add/remove> <proxyGroup/--all> <name>"));
            }

            return;
        }

        if (strings[0].equalsIgnoreCase("execute")) {
            if (strings.length > 3) {
                StringBuilder stringBuilder = new StringBuilder();
                Arrays.stream(strings)
                    .forEach(e -> stringBuilder.append(e).append(StringUtil.SPACE));

                ReformCloudAPIBungee.getInstance().getChannelHandler()
                    .sendPacketAsynchronous("ReformCloudController",
                        new PacketOutDispatchConsoleCommand(
                            stringBuilder.substring(0, stringBuilder.length() - 1)));
                commandSender.sendMessage(TextComponent.fromLegacyText(
                    ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                        .getMessage("internal-api-bungee-command-send-controller")
                ));
            } else {
                commandSender.sendMessage(TextComponent.fromLegacyText(
                    prefix + "/reformcloud execute <server/proxy> <name> <command>"));
            }

            return;
        }

        if (strings[0].equalsIgnoreCase("process")) {
            if (strings.length == 3) {
                StringBuilder stringBuilder = new StringBuilder();
                Arrays.stream(strings)
                    .forEach(e -> stringBuilder.append(e).append(StringUtil.SPACE));

                ReformCloudAPIBungee.getInstance().getChannelHandler()
                    .sendPacketAsynchronous("ReformCloudController",
                        new PacketOutDispatchConsoleCommand(
                            stringBuilder.substring(0, stringBuilder.length() - 1)));
                commandSender.sendMessage(TextComponent.fromLegacyText(
                    ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                        .getMessage("internal-api-bungee-command-send-controller")
                ));
            } else {
                commandSender.sendMessage(TextComponent
                    .fromLegacyText(prefix + "/reformcloud process <start/stop> <group/name>"));
            }

            return;
        }

        if (strings[0].equalsIgnoreCase("reload")) {
            ReformCloudAPIBungee.getInstance().getChannelHandler()
                .sendPacketAsynchronous("ReformCloudController",
                    new PacketOutDispatchConsoleCommand("reload"));
            commandSender.sendMessage(TextComponent.fromLegacyText(
                ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                    .getMessage("internal-api-bungee-command-send-controller")
            ));

            return;
        }

        if (strings[0].equalsIgnoreCase("list")) {
            if (strings.length != 3) {
                List<Client> connectedClients = new ArrayList<>();
                ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                    .getClients()
                    .values()
                    .stream()
                    .filter(client -> client.getClientInfo() != null)
                    .forEach(connectedClients::add);

                if (connectedClients.size() > 0) {
                    commandSender.sendMessage(TextComponent.fromLegacyText(prefix + "The following §eClients§7 are connected: "));
                    connectedClients.forEach(e -> {
                        final ClientInfo clientInfo = ReformCloudAPIBungee.getInstance().getConnectedClient(e.getName());
                        commandSender.sendMessage(TextComponent.fromLegacyText(
                            "    - §e" + e.getName() + "§7 | Host: §e" + e.getIp() + "§7 | Memory-Usage: §e"
                                + getMemoryOf(e.getName()) + "MB§7/§e" + clientInfo.getMaxMemory()
                                + "MB§7 | Processors: §e" + clientInfo.getCpuCoresSystem()
                                + "§7 | CPU-Usage: §e" + new DecimalFormat("##.###").format(clientInfo.getCpuUsage())
                                + "%§7 | Started-Processes: §e" + (clientInfo.getStartedProxies().size()
                                + clientInfo.getStartedServers().size())));
                        commandSender.sendMessage(TextComponent.fromLegacyText(""));
                    });

                    connectedClients.forEach(e -> {
                        if (ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                            .getServerProcessManager().getAllRegisteredProxyProcesses().stream()
                            .anyMatch(proxies -> proxies.getCloudProcess().getClient()
                                .equals(e.getName()))) {
                            commandSender.sendMessage(TextComponent.fromLegacyText(
                                prefix + "§7The following §eproxies§7 are started on \"§e" + e.getName() + "§7\": "));
                            ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                                .getServerProcessManager().getAllRegisteredProxyProcesses().stream()
                                .filter(proxyInfo -> proxyInfo.getCloudProcess().getClient()
                                    .equals(e.getName())).forEach(info -> commandSender.sendMessage(TextComponent.fromLegacyText(
                                "    - §e" + info.getCloudProcess().getName() + "§7 | Maintenance: §e" + info.getProxyGroup().isMaintenance() + "§7 | Player: §e" + info
                                    .getOnline() + "§7/§e" + info.getProxyGroup().getMaxPlayers())));
                            commandSender.sendMessage(TextComponent.fromLegacyText(""));
                        } else {
                            commandSender.sendMessage(TextComponent.fromLegacyText(prefix + "There are no started §eproxies§7 on \"§e" + e.getName() + "§7\""));
                            commandSender.sendMessage(TextComponent.fromLegacyText(""));
                        }

                        if (ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                            .getServerProcessManager().getAllRegisteredServerProcesses().stream()
                            .anyMatch(servers -> servers.getCloudProcess().getClient()
                                .equals(e.getName()))) {
                            commandSender.sendMessage(TextComponent.fromLegacyText(
                                prefix + "The following §eservers§7 are started on \"§e" + e.getName() + "§7\": "));
                            ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                                .getServerProcessManager().getAllRegisteredServerProcesses().stream()
                                .filter(serverInfo -> serverInfo.getCloudProcess().getClient()
                                    .equals(e.getName())).forEach(info -> commandSender.sendMessage(TextComponent.fromLegacyText(
                                "    - §e" + info.getCloudProcess().getName() + "§7 | State: §e" + info
                                    .getServerState() + "§7 | Player: §e" + info.getOnline() + "§7/§e" + info
                                    .getServerGroup().getMaxPlayers())));
                            commandSender.sendMessage(TextComponent.fromLegacyText(""));
                        } else {
                            commandSender.sendMessage(TextComponent.fromLegacyText(prefix + "There are no started §eservers§7 on \"§e" + e.getName() + "7r\""));
                            commandSender.sendMessage(TextComponent.fromLegacyText(""));
                        }
                    });
                } else
                    commandSender.sendMessage(TextComponent.fromLegacyText(prefix + "There are no §eClients§7 connected to the §eController"));
            } else {
                switch (strings[1].toLowerCase()) {
                    case "server": {
                        if (ReformCloudAPIBungee.getInstance().getServerGroup(strings[2]) != null) {
                            List<ServerInfo> connected = new ArrayList<>();
                            ReformCloudAPIBungee.getInstance()
                                .getInternalCloudNetwork()
                                .getServerProcessManager()
                                .getAllRegisteredServerProcesses()
                                .stream()
                                .filter(e -> e.getServerGroup().getName().equalsIgnoreCase(strings[2]))
                                .forEach(connected::add);

                            if (connected.size() > 0) {
                                commandSender.sendMessage(TextComponent.fromLegacyText(
                                    prefix + "The following §eservers§7 of the group \"§e" + strings[2]
                                        + "§7\" are connected: "));
                                connected.forEach(info -> commandSender.sendMessage(TextComponent.fromLegacyText(
                                    "    - §e" + info.getCloudProcess().getName() + "§7 | State: §e" + info
                                        .getServerState() + "§7 | Player: §e" + info.getOnline() + "§7/§e" + info
                                        .getServerGroup().getMaxPlayers())));
                            } else
                                commandSender.sendMessage(TextComponent.fromLegacyText(prefix + "There are no started §eservers§7 of the group \"§e" + strings[2] + "§7\""));
                        } else
                            commandSender.sendMessage(TextComponent.fromLegacyText(prefix + "§cThe servergroup doesn't exists"));

                        break;
                    }
                    case "proxy": {
                        if (ReformCloudAPIBungee.getInstance().getProxyGroup(strings[2]) != null) {
                            List<ProxyInfo> connected = new ArrayList<>();
                            ReformCloudAPIBungee.getInstance()
                                .getInternalCloudNetwork()
                                .getServerProcessManager()
                                .getAllRegisteredProxyProcesses()
                                .stream()
                                .filter(e -> e.getProxyGroup().getName().equalsIgnoreCase(strings[2]))
                                .forEach(connected::add);

                            if (connected.size() > 0) {
                                commandSender.sendMessage(TextComponent.fromLegacyText(
                                    prefix + "The following §eproxies§7 of the group \"§e" + strings[2]
                                        + "§7\" are connected: "));
                                connected.forEach(info -> commandSender.sendMessage(TextComponent.fromLegacyText(
                                    "    - §e" + info.getCloudProcess().getName() + "§7 | Maintenance: §e" + info.getProxyGroup().isMaintenance() + "§7 | Player: §e" + info
                                        .getOnline() + "§7/§e" + info.getProxyGroup().getMaxPlayers())));
                            } else
                                commandSender.sendMessage(TextComponent.fromLegacyText(prefix + "There are no started §eproxies§7 of the group \"§e" + strings[2] + "§7\""));
                        } else
                            commandSender.sendMessage(TextComponent.fromLegacyText(prefix + "§cThe proxygroup doesn't exists"));

                        break;
                    }
                    default: {
                        commandSender.sendMessage(TextComponent.fromLegacyText(prefix + "process list <server/proxy> <group-name>"));
                        break;
                    }
                }
            }
            return;
        }

        commandSender.sendMessage(TextComponent.fromLegacyText(
            ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                .getMessage("internal-api-bungee-command-reformcloud-invalid-syntax")));
        sendHelp(commandSender, prefix);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        if (!commandSender.hasPermission(getPermission())) {
            return new LinkedList<>();
        }

        if (strings.length == 2 && strings[0].equalsIgnoreCase("copy")) {
            return registered();
        }

        if (strings.length == 2 && strings[0].equalsIgnoreCase("whitelist")) {
            return Arrays.asList("add", "remove");
        }

        if (strings.length == 3 && strings[0].equalsIgnoreCase("whitelist")) {
            Collection<String> out = registeredProxyGroups();
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

    private Collection<String> registered() {
        Collection<String> out = new LinkedList<>();
        ReformCloudAPIBungee.getInstance().getAllRegisteredProxies()
            .forEach(e -> out.add(e.getCloudProcess().getName()));
        ReformCloudAPIBungee.getInstance().getAllRegisteredServers()
            .forEach(e -> out.add(e.getCloudProcess().getName()));
        return out;
    }

    private Collection<String> registeredServers() {
        Collection<String> out = new LinkedList<>();
        ReformCloudAPIBungee.getInstance().getAllRegisteredServers()
            .forEach(e -> out.add(e.getCloudProcess().getName()));
        return out;
    }

    private Collection<String> registeredProxies() {
        Collection<String> out = new LinkedList<>();
        ReformCloudAPIBungee.getInstance().getAllRegisteredProxies()
            .forEach(e -> out.add(e.getCloudProcess().getName()));
        return out;
    }

    private Collection<String> registeredGroups() {
        Collection<String> out = new LinkedList<>();
        ReformCloudAPIBungee.getInstance().getAllProxyGroups().forEach(e -> out.add(e.getName()));
        ReformCloudAPIBungee.getInstance().getAllServerGroups().forEach(e -> out.add(e.getName()));
        return out;
    }

    private Collection<String> registeredServerGroups() {
        Collection<String> out = new LinkedList<>();
        ReformCloudAPIBungee.getInstance().getAllServerGroups().forEach(e -> out.add(e.getName()));
        return out;
    }

    private Collection<String> registeredProxyGroups() {
        Collection<String> out = new LinkedList<>();
        ReformCloudAPIBungee.getInstance().getAllProxyGroups().forEach(e -> out.add(e.getName()));
        return out;
    }

    private Collection<String> players() {
        Collection<String> out = new LinkedList<>();
        BungeecordBootstrap.getInstance().getProxy().getPlayers()
            .forEach(e -> out.add(e.getName()));
        return out;
    }

    private void sendHelp(CommandSender commandSender, String prefix) {
        commandSender.sendMessage(
            new TextComponent(TextComponent.fromLegacyText(
                prefix + "§7/reformcloud copy <name> <file/dir> \n")),
            new TextComponent(TextComponent.fromLegacyText(
                prefix + "§7/reformcloud maintenance\n")),
            new TextComponent(TextComponent.fromLegacyText(
                prefix + "§7/reformcloud whitelist <add/remove> <proxyGroup/--all> <name> \n")),
            new TextComponent(TextComponent.fromLegacyText(
                prefix + "§7/reformcloud execute <server/proxy> <name> <command> \n")),
            new TextComponent(TextComponent.fromLegacyText(
                prefix + "§7/reformcloud process <start/stop> <group-name> \n")),
            new TextComponent(TextComponent.fromLegacyText(
                prefix + "§7/reformcloud list <server/proxy> <group-name> \n")),
            new TextComponent(TextComponent.fromLegacyText(
                prefix + "§7/reformcloud reload \n")),
            new TextComponent(TextComponent.fromLegacyText(
                prefix + "§7/reformcloud version"))
        );
    }

    private long getMemoryOf(String client) {
        AtomicLong atomicLong = new AtomicLong(0);
        ReformCloudAPIBungee.getInstance().getAllRegisteredProxies()
            .stream()
            .filter(e -> e.getCloudProcess().getClient().equals(client))
            .forEach(e -> atomicLong.addAndGet(e.getMaxMemory()));
        ReformCloudAPIBungee.getInstance().getAllRegisteredServers()
            .stream()
            .filter(e -> e.getCloudProcess().getClient().equals(client))
            .forEach(e -> atomicLong.addAndGet(e.getMaxMemory()));

        return atomicLong.get();
    }
}
