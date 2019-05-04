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

                commandSender.sendMessage(TextComponent.fromLegacyText(result));
            });
        }

        commandSender.sendMessage(TextComponent.fromLegacyText("perms list"));
        commandSender.sendMessage(TextComponent.fromLegacyText("perms <USERNAME> <ADDPERM/REMOVEPERM> <PERMISSION>"));
        commandSender.sendMessage(TextComponent.fromLegacyText("perms <USERNAME> <ADDGROUP/REMOVEGROUP/SETGROUP> <GROUPNAME>"));
        commandSender.sendMessage(TextComponent.fromLegacyText("perms <USERNAME> <ADDGROUP/SETGROUP> <GROUPNAME> <TIMEOUTINDAYS>"));
        commandSender.sendMessage(TextComponent.fromLegacyText("perms <GROUPNAME> setdefault"));
        commandSender.sendMessage(TextComponent.fromLegacyText("perms <GROUPNAME> <CREATE/DELETE>"));
        commandSender.sendMessage(TextComponent.fromLegacyText("perms <GROUPNAME> <ADD/REMOVE> <PERMISSION>"));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        if (!commandSender.hasPermission(getPermission()))
            return new LinkedList<>();

        return Arrays.asList("list");
    }
}
