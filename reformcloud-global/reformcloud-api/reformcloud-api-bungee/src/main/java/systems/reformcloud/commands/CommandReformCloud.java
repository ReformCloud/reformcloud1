/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.network.packets.PacketOutDispatchConsoleCommand;
import systems.reformcloud.utility.StringUtil;

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

        final String prefix = ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getPrefix() + "§7";

        if (strings.length == 0) {
            commandSender.sendMessage(TextComponent.fromLegacyText(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-reformcloud-invalid-syntax")));
            commandSender.sendMessage(
                    new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud copy <name> \n")),
                    new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud whitelist <add/remove> <proxyGroup/--all> <name> \n")),
                    new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud execute <server/proxy> <name> <command> \n")),
                    new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud process <start/stop> <group/name> \n")),
                    new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud reload \n")),
                    new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud version"))
            );
            return;
        }

        if (!commandSender.hasPermission("reformcloud.command." + strings[0])) {
            commandSender.sendMessage(TextComponent.fromLegacyText(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-reformcloud-no-permission")));
            return;
        }

        if (strings[0].equalsIgnoreCase("version")) {
            commandSender.sendMessage(TextComponent.fromLegacyText("You are using the ReformCloud V" + StringUtil.REFORM_VERSION + "@" + StringUtil.REFORM_SPECIFICATION));
            return;
        }

        if (strings[0].equalsIgnoreCase("copy")) {
            if (strings.length == 2) {
                ReformCloudAPIBungee.getInstance().getChannelHandler()
                        .sendPacketAsynchronous("ReformCloudController",
                                new PacketOutDispatchConsoleCommand("copy " + strings[1]));
                commandSender.sendMessage(TextComponent.fromLegacyText("The command was send to the controller"));
            } else
                commandSender.sendMessage(TextComponent.fromLegacyText(prefix + "/reformcloud copy <name>"));

            return;
        }

        if (strings[0].equalsIgnoreCase("whitelist")) {
            if (strings.length == 4) {
                StringBuilder stringBuilder = new StringBuilder();
                Arrays.stream(strings).forEach(e -> stringBuilder.append(e).append(StringUtil.SPACE));

                ReformCloudAPIBungee.getInstance().getChannelHandler()
                        .sendPacketAsynchronous("ReformCloudController",
                                new PacketOutDispatchConsoleCommand(stringBuilder.substring(0, stringBuilder.length() - 1)));
                commandSender.sendMessage(TextComponent.fromLegacyText("The command was send to the controller"));
            } else
                commandSender.sendMessage(TextComponent.fromLegacyText("/reformcloud whitelist <add/remove> <proxyGroup/--all> <name>"));

            return;
        }

        if (strings[0].equalsIgnoreCase("execute")) {
            if (strings.length > 3) {
                StringBuilder stringBuilder = new StringBuilder();
                Arrays.stream(strings).forEach(e -> stringBuilder.append(e).append(StringUtil.SPACE));

                ReformCloudAPIBungee.getInstance().getChannelHandler()
                        .sendPacketAsynchronous("ReformCloudController",
                                new PacketOutDispatchConsoleCommand(stringBuilder.substring(0, stringBuilder.length() - 1)));
                commandSender.sendMessage(TextComponent.fromLegacyText("The command was send to the controller"));
            } else
                commandSender.sendMessage(TextComponent.fromLegacyText("/reformcloud execute <server/proxy> <name> <command>"));

            return;
        }

        if (strings[0].equalsIgnoreCase("process")) {
            if (strings.length == 3) {
                StringBuilder stringBuilder = new StringBuilder();
                Arrays.stream(strings).forEach(e -> stringBuilder.append(e).append(StringUtil.SPACE));

                ReformCloudAPIBungee.getInstance().getChannelHandler()
                        .sendPacketAsynchronous("ReformCloudController",
                                new PacketOutDispatchConsoleCommand(stringBuilder.substring(0, stringBuilder.length() - 1)));
                commandSender.sendMessage(TextComponent.fromLegacyText("The command was send to the controller"));
            } else
                commandSender.sendMessage(TextComponent.fromLegacyText("/reformcloud process <start/stop> <group/name>"));

            return;
        }

        if (strings[0].equalsIgnoreCase("reload")) {
            ReformCloudAPIBungee.getInstance().getChannelHandler()
                    .sendPacketAsynchronous("ReformCloudController",
                            new PacketOutDispatchConsoleCommand("reload"));
            commandSender.sendMessage(TextComponent.fromLegacyText("The command was send to the controller"));
        }

        commandSender.sendMessage(TextComponent.fromLegacyText(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-reformcloud-invalid-syntax")));
        commandSender.sendMessage(
                new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud copy <name> \n")),
                new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud whitelist <add/remove> <proxyGroup/--all> <name> \n")),
                new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud execute <server/proxy> <name> <command> \n")),
                new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud process <start/stop> <group/name> \n")),
                new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud reload \n")),
                new TextComponent(TextComponent.fromLegacyText(prefix + "/reformcloud version"))
        );
    }
}
