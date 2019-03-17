/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.launcher.BungeecordBootstrap;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.info.ServerInfo;

/**
 * @author _Klaro | Pasqual K. / created on 11.12.2018
 */

public final class CommandHub extends Command {
    public CommandHub() {
        super("hub");
    }

    @Override
    public String[] getAliases() {
        return new String[]{"l", "lobby", "leave"};
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof ProxiedPlayer)) return;

        final ProxiedPlayer proxiedPlayer = (ProxiedPlayer) commandSender;
        if (ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getServerProcessManager()
                .getRegisteredServerByName(proxiedPlayer.getServer().getInfo().getName()) != null
                && ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getServerProcessManager()
                .getRegisteredServerByName(proxiedPlayer.getServer().getInfo().getName())
                .getServerGroup().getServerModeType().equals(ServerModeType.LOBBY)) {
            proxiedPlayer.sendMessage(ChatMessageType.CHAT, TextComponent.fromLegacyText(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-hub-already")));
            return;
        }

        final ServerInfo fallback = ReformCloudAPIBungee.getInstance().nextFreeLobby(
                ReformCloudAPIBungee.getInstance().getProxyInfo().getProxyGroup(), proxiedPlayer
        );
        if (fallback == null)
            proxiedPlayer.sendMessage(ChatMessageType.CHAT,
                    TextComponent.fromLegacyText(ReformCloudAPIBungee.getInstance()
                            .getInternalCloudNetwork().getMessage("internal-api-bungee-command-hub-not-available")));
        else
            proxiedPlayer.connect(BungeecordBootstrap.getInstance().getProxy()
                    .getServerInfo(fallback.getCloudProcess().getName()));
    }
}
