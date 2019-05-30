/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.bootstrap.VelocityBootstrap;
import systems.reformcloud.network.packets.PacketOutExecuteCommandSilent;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 01.05.2019
 */

public final class CommandPermissions implements Serializable, Command {
    @Override
    public void execute(CommandSource commandSource, @NonNull String[] strings) {
        if (!commandSource.hasPermission("reformcloud.command.permissions")) {
            commandSource.sendMessage(TextComponent.of(ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-no-permission")));
            return;
        }

        if (strings.length == 1 && strings[0].equalsIgnoreCase("list")) {
            VelocityBootstrap.getInstance().getProxy().getScheduler().buildTask(VelocityBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIVelocity.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms list"),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSource.sendMessage(TextComponent.of("§cAn error occurred"));
                    return;
                }

                commandSource.sendMessage(TextComponent.of("§7" + result));
            }).schedule();
            return;
        } else if (strings.length == 2 && strings[1].equalsIgnoreCase("create")) {
            VelocityBootstrap.getInstance().getProxy().getScheduler().buildTask(VelocityBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIVelocity.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " create"),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSource.sendMessage(TextComponent.of("§cAn error occurred"));
                    return;
                }

                commandSource.sendMessage(TextComponent.of("§7" + result));
            }).schedule();
            return;
        } else if (strings.length == 2 && strings[1].equalsIgnoreCase("delete")) {
            VelocityBootstrap.getInstance().getProxy().getScheduler().buildTask(VelocityBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIVelocity.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " delete"),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSource.sendMessage(TextComponent.of("§cAn error occurred"));
                    return;
                }

                commandSource.sendMessage(TextComponent.of("§7" + result));
            }).schedule();
            return;
        } else if (strings.length == 2 && strings[1].equalsIgnoreCase("list")) {
            VelocityBootstrap.getInstance().getProxy().getScheduler().buildTask(VelocityBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIVelocity.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " list"),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSource.sendMessage(TextComponent.of("§cAn error occurred"));
                    return;
                }

                commandSource.sendMessage(TextComponent.of("§7" + result));
            }).schedule();
            return;
        } else if (strings.length == 3 && strings[1].equalsIgnoreCase("addperm")) {
            VelocityBootstrap.getInstance().getProxy().getScheduler().buildTask(VelocityBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIVelocity.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " addperm " + strings[2]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSource.sendMessage(TextComponent.of("§cAn error occurred"));
                    return;
                }

                commandSource.sendMessage(TextComponent.of("§7" + result));
            }).schedule();
            return;
        } else if (strings.length == 3 && strings[1].equalsIgnoreCase("removeperm")) {
            VelocityBootstrap.getInstance().getProxy().getScheduler().buildTask(VelocityBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIVelocity.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " removeperm " + strings[2]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSource.sendMessage(TextComponent.of("§cAn error occurred"));
                    return;
                }

                commandSource.sendMessage(TextComponent.of("§7" + result));
            }).schedule();
            return;
        } else if (strings.length == 3 && strings[1].equalsIgnoreCase("add")) {
            VelocityBootstrap.getInstance().getProxy().getScheduler().buildTask(VelocityBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIVelocity.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " remove " + strings[2]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSource.sendMessage(TextComponent.of("§cAn error occurred"));
                    return;
                }

                commandSource.sendMessage(TextComponent.of("§7" + result));
            }).schedule();
            return;
        } else if (strings.length == 3 && strings[1].equalsIgnoreCase("remove")) {
            VelocityBootstrap.getInstance().getProxy().getScheduler().buildTask(VelocityBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIVelocity.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " add " + strings[2]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSource.sendMessage(TextComponent.of("§cAn error occurred"));
                    return;
                }

                commandSource.sendMessage(TextComponent.of("§7" + result));
            }).schedule();
            return;
        } else if (strings.length == 3 && strings[1].equalsIgnoreCase("addgroup")) {
            VelocityBootstrap.getInstance().getProxy().getScheduler().buildTask(VelocityBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIVelocity.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " addgroup " + strings[2]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSource.sendMessage(TextComponent.of("§cAn error occurred"));
                    return;
                }

                commandSource.sendMessage(TextComponent.of("§7" + result));
            }).schedule();
            return;
        } else if (strings.length == 3 && strings[1].equalsIgnoreCase("removegroup")) {
            VelocityBootstrap.getInstance().getProxy().getScheduler().buildTask(VelocityBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIVelocity.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " removegroup " + strings[2]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSource.sendMessage(TextComponent.of("§cAn error occurred"));
                    return;
                }

                commandSource.sendMessage(TextComponent.of("§7" + result));
            }).schedule();
            return;
        } else if (strings.length == 3 && strings[1].equalsIgnoreCase("setgroup")) {
            VelocityBootstrap.getInstance().getProxy().getScheduler().buildTask(VelocityBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIVelocity.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " setgroup " + strings[2]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSource.sendMessage(TextComponent.of("§cAn error occurred"));
                    return;
                }

                commandSource.sendMessage(TextComponent.of("§7" + result));
            }).schedule();
            return;
        } else if (strings.length == 2 && strings[1].equalsIgnoreCase("setdefault")) {
            VelocityBootstrap.getInstance().getProxy().getScheduler().buildTask(VelocityBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIVelocity.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " setdefault"),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSource.sendMessage(TextComponent.of("§cAn error occurred"));
                    return;
                }

                commandSource.sendMessage(TextComponent.of("§7" + result));
            }).schedule();
            return;
        } else if (strings.length == 4 && strings[1].equalsIgnoreCase("addgroup")) {
            VelocityBootstrap.getInstance().getProxy().getScheduler().buildTask(VelocityBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIVelocity.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " addgroup " + strings[2] + " " + strings[3]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSource.sendMessage(TextComponent.of("§cAn error occurred"));
                    return;
                }

                commandSource.sendMessage(TextComponent.of("§7" + result));
            }).schedule();
            return;
        } else if (strings.length == 4 && strings[1].equalsIgnoreCase("setgroup")) {
            VelocityBootstrap.getInstance().getProxy().getScheduler().buildTask(VelocityBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIVelocity.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " setgroup " + strings[2] + " " + strings[3]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSource.sendMessage(TextComponent.of("§cAn error occurred"));
                    return;
                }

                commandSource.sendMessage(TextComponent.of("§7" + result));
            }).schedule();
            return;
        } else if (strings.length == 3 && strings[1].equalsIgnoreCase("setprefix")) {
            VelocityBootstrap.getInstance().getProxy().getScheduler().buildTask(VelocityBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIVelocity.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " setprefix " + strings[2]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSource.sendMessage(TextComponent.of("§cAn error occurred"));
                    return;
                }

                commandSource.sendMessage(TextComponent.of("§7" + result));
            });
            return;
        } else if (strings.length == 3 && strings[1].equalsIgnoreCase("setsuffix")) {
            VelocityBootstrap.getInstance().getProxy().getScheduler().buildTask(VelocityBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIVelocity.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " setsuffix " + strings[2]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSource.sendMessage(TextComponent.of("§cAn error occurred"));
                    return;
                }

                commandSource.sendMessage(TextComponent.of("§7" + result));
            });
            return;
        } else if (strings.length == 3 && strings[1].equalsIgnoreCase("setdisplay")) {
            VelocityBootstrap.getInstance().getProxy().getScheduler().buildTask(VelocityBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIVelocity.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " setdisplay " + strings[2]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSource.sendMessage(TextComponent.of("§cAn error occurred"));
                    return;
                }

                commandSource.sendMessage(TextComponent.of("§7" + result));
            });
            return;
        } else if (strings.length == 3 && strings[1].equalsIgnoreCase("settabcolorcode")) {
            VelocityBootstrap.getInstance().getProxy().getScheduler().buildTask(VelocityBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIVelocity.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " settabcolorcode " + strings[2]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSource.sendMessage(TextComponent.of("§cAn error occurred"));
                    return;
                }

                commandSource.sendMessage(TextComponent.of("§7" + result));
            });
            return;
        } else if (strings.length == 3 && strings[1].equalsIgnoreCase("setgroupid")) {
            VelocityBootstrap.getInstance().getProxy().getScheduler().buildTask(VelocityBootstrap.getInstance(), () -> {
                String result = ReformCloudAPIVelocity.getInstance().createPacketFuture(
                        new PacketOutExecuteCommandSilent("perms " + strings[0] + " setgroupid " + strings[2]),
                        "ReformCloudController"
                ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
                if (result == null) {
                    commandSource.sendMessage(TextComponent.of("§cAn error occurred"));
                    return;
                }

                commandSource.sendMessage(TextComponent.of("§7" + result));
            });
            return;
        }

        String prefix = ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getPrefix();
        commandSource.sendMessage(TextComponent.of(prefix + (prefix.endsWith(" ") ? "" : " ") + "§7perms list"));
        commandSource.sendMessage(TextComponent.of(prefix + (prefix.endsWith(" ") ? "" : " ") + "§7perms <USERNAME> list"));
        commandSource.sendMessage(TextComponent.of(prefix + (prefix.endsWith(" ") ? "" : " ") + "§7perms <USERNAME> <ADDPERM/REMOVEPERM> <PERMISSION>"));
        commandSource.sendMessage(TextComponent.of(prefix + (prefix.endsWith(" ") ? "" : " ") + "§7perms <USERNAME> <ADDGROUP/REMOVEGROUP/SETGROUP> <GROUPNAME>"));
        commandSource.sendMessage(TextComponent.of(prefix + (prefix.endsWith(" ") ? "" : " ") + "§7perms <USERNAME> <ADDGROUP/SETGROUP> <GROUPNAME> <TIMEOUTINDAYS>"));
        commandSource.sendMessage(TextComponent.of(prefix + (prefix.endsWith(" ") ? "" : " ") + "§7perms <GROUPNAME> setdefault"));
        commandSource.sendMessage(TextComponent.of(prefix + (prefix.endsWith(" ") ? "" : " ") + "§7perms <GROUPNAME> <CREATE/DELETE>"));
        commandSource.sendMessage(TextComponent.of(prefix + (prefix.endsWith(" ") ? "" : " ") + "§7perms <GROUPNAME> <ADD/REMOVE> <PERMISSION>"));
        commandSource.sendMessage(TextComponent.of(prefix + (prefix.endsWith(" ") ? "" : " ") + "§7perms <GROUPNAME> <SETPREFIX/SETSUFFIX/SETDISPLAY/SETTABCOLORCODE/SETGROUPID> <VALUE>"));
    }

    @Override
    public List<String> suggest(CommandSource source, @NonNull String[] strings) {
        if (!(source instanceof Player))
            return new LinkedList<>();

        if (!source.hasPermission("reformcloud.command.permissions"))
            return new LinkedList<>();

        if (strings.length == 2) {
            return Arrays.asList("list", "addperm", "removeperm", "addgroup", "removegroup", "setgroup", "setdefault", "create", "delete", "add", "remove", "setprefix", "setsuffix", "setdisplay", "settabcolorcode", "setgroupid");
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

        return defaultHelp((Player) source);
    }

    @Override
    public boolean hasPermission(CommandSource source, @NonNull String[] args) {
        System.out.println("Check PERMS");
        System.out.println(source);
        System.out.println(source.getPermissionValue("reformcloud.command.permissions").asBoolean());
        return source.getPermissionValue("reformcloud.command.permissions").asBoolean();
    }

    private List<String> defaultHelp(Player player) {
        List<String> out = new LinkedList<>();
        out.add("list");
        out.add(player.getUsername());
        out.addAll(groupNames());
        return out;
    }

    private List<String> groupNames() {
        List<String> out = new LinkedList<>();
        ReformCloudAPIVelocity.getInstance().getPermissionCache().getAllGroupsAndDefault().forEach(e -> out.add(e.getName()));
        return out;
    }
}
