/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.netty.packets.PacketOutDispatchConsoleCommand;
import systems.reformcloud.utility.StringUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public final class CommandReformCloud extends Command {
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
            commandSender.sendMessage(TextComponent.fromLegacyText(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-no-permission")));
            return;
        }

        if (strings.length == 0) {
            final String prefix = ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getPrefix() + "§7";

            commandSender.sendMessage(TextComponent.fromLegacyText(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-reformcloud-invalid-syntax")));
            commandSender.sendMessage(
                    new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud process <start/stop> <name> \n")),
                    new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud clear \n")),
                    new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud update \n")),
                    new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud whitelist <add/remove> <proxyGroup/--all> <name> \n")),
                    new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud execute <server/proxy> <name> <command> \n")),
                    new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud delete <servergroup/proxygroup/client> <name> \n")),
                    new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud exit \n")),
                    new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud reload\n")),
                    new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud create client <name> <ip> \n")),
                    new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud create servergroup <name> <client> \n")),
                    new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud create proxygroup <name> <client> <host> \n")),
                    new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud copy <serverName> \n")),
                    new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud info"))
            );
            return;
        }

        if (!commandSender.hasPermission("reformcloud.command." + strings[0])) {
            commandSender.sendMessage(TextComponent.fromLegacyText(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-reformcloud-no-permission")));
            return;
        }

        if (strings[0].equalsIgnoreCase("info")) {
            commandSender.sendMessage(TextComponent.fromLegacyText("You are using the ReformCloud V" + StringUtil.REFORM_VERSION + "@" + StringUtil.REFORM_SPECIFICATION));
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(strings).forEach(e -> stringBuilder.append(e).append(StringUtil.SPACE));

        ReformCloudAPIBungee.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController", new PacketOutDispatchConsoleCommand(stringBuilder.substring(0, stringBuilder.length() - 1)));
        commandSender.sendMessage(TextComponent.fromLegacyText(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-reformcloud-command-success")));
    }
}
