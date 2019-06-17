/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.launcher.BungeecordBootstrap;
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

        final String prefix = ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
            .getPrefix();

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
                .fromLegacyText("§7You are using the ReformCloud V" + StringUtil.REFORM_VERSION));
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
            Collection<String> out = proxies();
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
                return registeredSevers();
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

        return Arrays.asList("copy", "whitelist", "execute", "process",
            "reload", "version", "maintenance");
    }

    private Collection<String> registered() {
        Collection<String> out = new LinkedList<>();
        ReformCloudAPIBungee.getInstance().getAllRegisteredProxies()
            .forEach(e -> out.add(e.getCloudProcess().getName()));
        ReformCloudAPIBungee.getInstance().getAllRegisteredServers()
            .forEach(e -> out.add(e.getCloudProcess().getName()));
        return out;
    }

    private Collection<String> registeredSevers() {
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

    private Collection<String> proxies() {
        Collection<String> out = new LinkedList<>();
        ReformCloudAPIBungee.getInstance().getAllProxyGroups().forEach(e -> out.add(e.getName()));
        return out;
    }

    private Collection<String> registeredGroups() {
        Collection<String> out = new LinkedList<>();
        ReformCloudAPIBungee.getInstance().getAllProxyGroups().forEach(e -> out.add(e.getName()));
        ReformCloudAPIBungee.getInstance().getAllServerGroups().forEach(e -> out.add(e.getName()));
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
                prefix + (prefix.endsWith(" ") ? "" : " ") + "§7/reformcloud copy <name> \n")),
            new TextComponent(TextComponent.fromLegacyText(
                prefix + (prefix.endsWith(" ") ? "" : " ") + "§7/reformcloud "
                    + "maintenance\n")),
            new TextComponent(TextComponent.fromLegacyText(
                prefix + (prefix.endsWith(" ") ? "" : " ")
                    + "§7/reformcloud copy <name> <file/dir> \n")),
            new TextComponent(TextComponent.fromLegacyText(
                prefix + (prefix.endsWith(" ") ? "" : " ")
                    + "§7/reformcloud whitelist <add/remove> <proxyGroup/--all> <name> \n")),
            new TextComponent(TextComponent.fromLegacyText(
                prefix + (prefix.endsWith(" ") ? "" : " ")
                    + "§7/reformcloud execute <server/proxy> <name> <command> \n")),
            new TextComponent(TextComponent.fromLegacyText(
                prefix + (prefix.endsWith(" ") ? "" : " ")
                    + "§7/reformcloud process <start/stop> <name> \n")),
            new TextComponent(TextComponent.fromLegacyText(
                prefix + (prefix.endsWith(" ") ? "" : " ") + "§7/reformcloud reload \n")),
            new TextComponent(TextComponent.fromLegacyText(
                prefix + (prefix.endsWith(" ") ? "" : " ") + "§7/reformcloud version"))
        );
    }
}
