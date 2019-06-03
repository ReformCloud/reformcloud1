/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.optional.qual.MaybePresent;
import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.bootstrap.VelocityBootstrap;
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

        final String prefix =
            ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getPrefix() + "§7";

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

        if (strings.length == 2 && strings[0].equalsIgnoreCase("copy")) {
            return registered();
        }

        if (strings.length == 2 && strings[0].equalsIgnoreCase("whitelist")) {
            return Arrays.asList("add", "remove");
        }

        if (strings.length == 3 && strings[0].equalsIgnoreCase("whitelist")) {
            List<String> out = proxies();
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

        return Arrays.asList("copy", "whitelist", "execute", "process", "reload", "version");
    }

    private List<String> registered() {
        List<String> out = new LinkedList<>();
        ReformCloudAPIVelocity.getInstance().getAllRegisteredProxies()
            .forEach(e -> out.add(e.getCloudProcess().getName()));
        ReformCloudAPIVelocity.getInstance().getAllRegisteredServers()
            .forEach(e -> out.add(e.getCloudProcess().getName()));
        return out;
    }

    private List<String> proxies() {
        List<String> out = new LinkedList<>();
        ReformCloudAPIVelocity.getInstance().getAllProxyGroups().forEach(e -> out.add(e.getName()));
        return out;
    }

    private List<String> registeredSevers() {
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

    private List<String> players() {
        List<String> out = new LinkedList<>();
        VelocityBootstrap.getInstance().getProxy().getAllPlayers()
            .forEach(e -> out.add(e.getUsername()));
        return out;
    }

    private void sendHelp(CommandSource commandSource, String prefix) {
        commandSource.sendMessage(TextComponent
            .of(prefix + (prefix.endsWith(" ") ? "" : " ") + "§7/reformcloud copy <name> \n"));
        commandSource.sendMessage(TextComponent.of(prefix + (prefix.endsWith(" ") ? "" : " ")
            + "§7/reformcloud copy <name> <file/dir> \n"));
        commandSource.sendMessage(TextComponent.of(prefix + (prefix.endsWith(" ") ? "" : " ")
            + "§7/reformcloud whitelist <add/remove> <proxyGroup/--all> <name> \n"));
        commandSource.sendMessage(TextComponent.of(prefix + (prefix.endsWith(" ") ? "" : " ")
            + "§7/reformcloud execute <server/proxy> <name> <command> \n"));
        commandSource.sendMessage(TextComponent.of(prefix + (prefix.endsWith(" ") ? "" : " ")
            + "§7/reformcloud process <start/stop> <group/name> \n"));
        commandSource.sendMessage(TextComponent
            .of(prefix + (prefix.endsWith(" ") ? "" : " ") + "§7/reformcloud reload \n"));
        commandSource.sendMessage(TextComponent
            .of(prefix + (prefix.endsWith(" ") ? "" : " ") + "§7/reformcloud version"));
    }
}
