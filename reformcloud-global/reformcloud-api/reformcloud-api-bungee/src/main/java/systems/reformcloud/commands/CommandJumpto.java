/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.launcher.BungeecordBootstrap;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public final class CommandJumpto extends Command implements TabExecutor {
    public CommandJumpto() {
        super("jumpto", "reformcloud.command.jumpto", "goto", "jt", "gt");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof ProxiedPlayer)) return;
        final ProxiedPlayer proxiedPlayer = (ProxiedPlayer) commandSender;

        if (strings.length == 1) {
            if (BungeecordBootstrap.getInstance().getProxy().getServers().get(strings[0]) == null
                    && BungeecordBootstrap.getInstance().getProxy().getPlayer(strings[0]) == null) {
                commandSender.sendMessage(TextComponent.fromLegacyText(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-jumpto-server-player-not-found")));
                return;
            }

            if (BungeecordBootstrap.getInstance().getProxy().getPlayer(strings[0]) != null) {
                proxiedPlayer.connect(BungeecordBootstrap.getInstance().getProxy().getServerInfo(BungeecordBootstrap.getInstance().getProxy().getPlayer(strings[0]).getServer().getInfo().getName()));
                proxiedPlayer.sendMessage(ChatMessageType.CHAT, TextComponent.fromLegacyText(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-jumpto-success")));
                return;
            }

            if (BungeecordBootstrap.getInstance().getProxy().getServers().get(strings[0]) != null) {
                proxiedPlayer.connect(BungeecordBootstrap.getInstance().getProxy().getServers().get(strings[0]));
                proxiedPlayer.sendMessage(ChatMessageType.CHAT, TextComponent.fromLegacyText(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-jumpto-success")));
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        StringBuilder stringBuilder = new StringBuilder();

        Arrays.stream(strings).forEach(s -> stringBuilder.append(s));

        LinkedList<String> iterable = new LinkedList<>();
        ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredServerNameProcesses().stream().filter(e -> e.startsWith(stringBuilder.substring(0))).forEach(iterable::add);
        BungeecordBootstrap.getInstance().getProxy().getPlayers().stream().filter(e -> e.getName().startsWith(stringBuilder.substring(0))).forEach(e -> iterable.add(e.getName()));

        return iterable;
    }
}
