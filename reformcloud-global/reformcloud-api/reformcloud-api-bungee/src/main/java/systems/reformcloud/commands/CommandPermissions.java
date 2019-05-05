/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.launcher.BungeecordBootstrap;
import systems.reformcloud.network.packets.PacketOutExecuteCommandSilent;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 01.05.2019
 */

public final class CommandPermissions extends Command implements Serializable, TabExecutor {
    public CommandPermissions() {
        super("perms", "reformcloud.command.perms", "cloudperms", "cp", "permissions");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length == 1 && strings[0].equalsIgnoreCase("list")) {
            BungeecordBootstrap.getInstance().getProxy().getScheduler().runAsync(BungeecordBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIBungee.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms list"),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSender.sendMessage(TextComponent.fromLegacyText("§cAn error occurred"));
                    return;
                }

                commandSender.sendMessage(TextComponent.fromLegacyText("§7" + result));
            });
            return;
        } else if (strings.length == 2 && strings[1].equalsIgnoreCase("create")) {
            BungeecordBootstrap.getInstance().getProxy().getScheduler().runAsync(BungeecordBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIBungee.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " create"),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSender.sendMessage(TextComponent.fromLegacyText("§cAn error occurred"));
                    return;
                }

                commandSender.sendMessage(TextComponent.fromLegacyText("§7" + result));
            });
            return;
        } else if (strings.length == 2 && strings[1].equalsIgnoreCase("delete")) {
            BungeecordBootstrap.getInstance().getProxy().getScheduler().runAsync(BungeecordBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIBungee.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " delete"),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSender.sendMessage(TextComponent.fromLegacyText("§cAn error occurred"));
                    return;
                }

                commandSender.sendMessage(TextComponent.fromLegacyText("§7" + result));
            });
            return;
        } else if (strings.length == 2 && strings[1].equalsIgnoreCase("list")) {
            BungeecordBootstrap.getInstance().getProxy().getScheduler().runAsync(BungeecordBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIBungee.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " list"),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSender.sendMessage(TextComponent.fromLegacyText("§cAn error occurred"));
                    return;
                }

                commandSender.sendMessage(TextComponent.fromLegacyText("§7" + result));
            });
            return;
        } else if (strings.length == 3 && strings[1].equalsIgnoreCase("addperm")) {
            BungeecordBootstrap.getInstance().getProxy().getScheduler().runAsync(BungeecordBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIBungee.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " addperm " + strings[2]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSender.sendMessage(TextComponent.fromLegacyText("§cAn error occurred"));
                    return;
                }

                commandSender.sendMessage(TextComponent.fromLegacyText("§7" + result));
            });
            return;
        } else if (strings.length == 3 && strings[1].equalsIgnoreCase("removeperm")) {
            BungeecordBootstrap.getInstance().getProxy().getScheduler().runAsync(BungeecordBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIBungee.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " removeperm " + strings[2]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSender.sendMessage(TextComponent.fromLegacyText("§cAn error occurred"));
                    return;
                }

                commandSender.sendMessage(TextComponent.fromLegacyText("§7" + result));
            });
            return;
        } else if (strings.length == 3 && strings[1].equalsIgnoreCase("add")) {
            BungeecordBootstrap.getInstance().getProxy().getScheduler().runAsync(BungeecordBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIBungee.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " add " + strings[2]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSender.sendMessage(TextComponent.fromLegacyText("§cAn error occurred"));
                    return;
                }

                commandSender.sendMessage(TextComponent.fromLegacyText("§7" + result));
            });
            return;
        } else if (strings.length == 3 && strings[1].equalsIgnoreCase("remove")) {
            BungeecordBootstrap.getInstance().getProxy().getScheduler().runAsync(BungeecordBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIBungee.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " remove " + strings[2]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSender.sendMessage(TextComponent.fromLegacyText("§cAn error occurred"));
                    return;
                }

                commandSender.sendMessage(TextComponent.fromLegacyText("§7" + result));
            });
            return;
        } else if (strings.length == 3 && strings[1].equalsIgnoreCase("addgroup")) {
            BungeecordBootstrap.getInstance().getProxy().getScheduler().runAsync(BungeecordBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIBungee.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " addgroup " + strings[2]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSender.sendMessage(TextComponent.fromLegacyText("§cAn error occurred"));
                    return;
                }

                commandSender.sendMessage(TextComponent.fromLegacyText("§7" + result));
            });
            return;
        } else if (strings.length == 3 && strings[1].equalsIgnoreCase("removegroup")) {
            BungeecordBootstrap.getInstance().getProxy().getScheduler().runAsync(BungeecordBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIBungee.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " removegroup " + strings[2]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSender.sendMessage(TextComponent.fromLegacyText("§cAn error occurred"));
                    return;
                }

                commandSender.sendMessage(TextComponent.fromLegacyText("§7" + result));
            });
            return;
        } else if (strings.length == 3 && strings[1].equalsIgnoreCase("setgroup")) {
            BungeecordBootstrap.getInstance().getProxy().getScheduler().runAsync(BungeecordBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIBungee.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " setgroup " + strings[2]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSender.sendMessage(TextComponent.fromLegacyText("§cAn error occurred"));
                    return;
                }

                commandSender.sendMessage(TextComponent.fromLegacyText("§7" + result));
            });
            return;
        } else if (strings.length == 2 && strings[1].equalsIgnoreCase("setdefault")) {
            BungeecordBootstrap.getInstance().getProxy().getScheduler().runAsync(BungeecordBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIBungee.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " setdefault"),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSender.sendMessage(TextComponent.fromLegacyText("§cAn error occurred"));
                    return;
                }

                commandSender.sendMessage(TextComponent.fromLegacyText("§7" + result));
            });
            return;
        } else if (strings.length == 4 && strings[1].equalsIgnoreCase("addgroup")) {
            BungeecordBootstrap.getInstance().getProxy().getScheduler().runAsync(BungeecordBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIBungee.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " addgroup " + strings[2] + " " + strings[3]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSender.sendMessage(TextComponent.fromLegacyText("§cAn error occurred"));
                    return;
                }

                commandSender.sendMessage(TextComponent.fromLegacyText("§7" + result));
            });
            return;
        } else if (strings.length == 4 && strings[1].equalsIgnoreCase("setgroup")) {
            BungeecordBootstrap.getInstance().getProxy().getScheduler().runAsync(BungeecordBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIBungee.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " setgroup " + strings[2] + " " + strings[3]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSender.sendMessage(TextComponent.fromLegacyText("§cAn error occurred"));
                    return;
                }

                commandSender.sendMessage(TextComponent.fromLegacyText("§7" + result));
            });
            return;
        }

        String prefix = ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getPrefix();
        commandSender.sendMessage(TextComponent.fromLegacyText(prefix + (prefix.endsWith(" ") ? "" : " ") + "§7perms list"));
        commandSender.sendMessage(TextComponent.fromLegacyText(prefix + (prefix.endsWith(" ") ? "" : " ") + "§7perms <USERNAME> list"));
        commandSender.sendMessage(TextComponent.fromLegacyText(prefix + (prefix.endsWith(" ") ? "" : " ") + "§7perms <USERNAME> <ADDPERM/REMOVEPERM> <PERMISSION>"));
        commandSender.sendMessage(TextComponent.fromLegacyText(prefix + (prefix.endsWith(" ") ? "" : " ") + "§7perms <USERNAME> <ADDGROUP/REMOVEGROUP/SETGROUP> <GROUPNAME>"));
        commandSender.sendMessage(TextComponent.fromLegacyText(prefix + (prefix.endsWith(" ") ? "" : " ") + "§7perms <USERNAME> <ADDGROUP/SETGROUP> <GROUPNAME> <TIMEOUTINDAYS>"));
        commandSender.sendMessage(TextComponent.fromLegacyText(prefix + (prefix.endsWith(" ") ? "" : " ") + "§7perms <GROUPNAME> setdefault"));
        commandSender.sendMessage(TextComponent.fromLegacyText(prefix + (prefix.endsWith(" ") ? "" : " ") + "§7perms <GROUPNAME> <CREATE/DELETE>"));
        commandSender.sendMessage(TextComponent.fromLegacyText(prefix + (prefix.endsWith(" ") ? "" : " ") + "§7perms <GROUPNAME> <ADD/REMOVE> <PERMISSION>"));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        if (!commandSender.hasPermission(getPermission()))
            return new LinkedList<>();

        if (strings.length == 2) {
            return Arrays.asList("list", "addperm", "removeperm", "addgroup", "removegroup", "setgroup", "setdefault", "create", "delete", "add", "remove");
        }

        if (strings.length == 3
                && (strings[1].equalsIgnoreCase("addperm")
                || strings[1].equalsIgnoreCase("removeperm")
                || strings[1].equalsIgnoreCase("add")
                || strings[1].equalsIgnoreCase("remove"))) {
            return Arrays.asList("reformcloud.command.jumpto", "reformcloud.command.perms", "reformcloud.command.reformcloud", "reformcloud.command.whereiam");
        }

        if (strings.length == 3
                && (strings[1].equalsIgnoreCase("addgroup")
                || strings[1].equalsIgnoreCase("setgroup")
                || strings[1].equalsIgnoreCase("removegroup"))) {
            return groupNames();
        }

        if (strings.length == 4 && (strings[1].equalsIgnoreCase("addgroup") || strings[1].equalsIgnoreCase("setgroup"))) {
            return Arrays.asList("1", "2", "3", "4", "5");
        }

        return defaultHelp(commandSender);
    }

    private List<String> defaultHelp(CommandSender commandSender) {
        List<String> out = new LinkedList<>();
        out.add("list");
        out.add(commandSender.getName());
        out.addAll(groupNames());
        return out;
    }

    private List<String> groupNames() {
        List<String> out = new LinkedList<>();
        ReformCloudAPIBungee.getInstance().getPermissionCache().getAllGroupsAndDefault().forEach(e -> out.add(e.getName()));
        return out;
    }
}
