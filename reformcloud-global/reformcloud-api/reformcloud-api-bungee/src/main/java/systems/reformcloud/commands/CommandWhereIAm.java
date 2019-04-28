/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.meta.info.ServerInfo;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.02.2019
 */

public final class CommandWhereIAm extends Command implements Serializable {
    public CommandWhereIAm() {
        super("whereiam", "reformcloud.command.whereiam", "whereami");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage(TextComponent.fromLegacyText(
                    ChatColor.translateAlternateColorCodes('&',
                            ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getPrefix() + " An §cerror §7occurred"))
            );
            return;
        }

        final ProxiedPlayer proxiedPlayer = (ProxiedPlayer) commandSender;
        if (proxiedPlayer.getServer() == null || proxiedPlayer.getServer().getInfo() == null) {
            proxiedPlayer.sendMessage(TextComponent.fromLegacyText(
                    ChatColor.translateAlternateColorCodes('&',
                            ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getPrefix() + " An §cerror §7occurred"))
            );
            return;
        }

        ServerInfo serverInfo = ReformCloudAPIBungee.getInstance()
                .getInternalCloudNetwork()
                .getServerProcessManager()
                .getRegisteredServerByName(proxiedPlayer.getServer().getInfo().getName());
        if (serverInfo == null) {
            proxiedPlayer.sendMessage(TextComponent.fromLegacyText(
                    ChatColor.translateAlternateColorCodes('&',
                            ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getPrefix() + " An §cerror §7occurred"))
            );
            return;
        }

        proxiedPlayer.sendMessage(TextComponent.fromLegacyText(
                ChatColor.translateAlternateColorCodes('&',
                        ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getPrefix() +
                                " You are currently connected to §e" + serverInfo.getCloudProcess().getName() +
                                " §7on ServerGroup §e" + serverInfo.getServerGroup().getName() + "§7 (Process UniqueID: §e" +
                                serverInfo.getCloudProcess().getProcessUID() + ")")
                )
        );
    }
}
