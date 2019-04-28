/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.optional.qual.MaybePresent;
import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.network.packets.PacketOutDispatchConsoleCommand;
import systems.reformcloud.utility.StringUtil;

import java.util.Arrays;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public final class CommandReformCloud implements Command {
    @Override
    public void execute(@MaybePresent CommandSource commandSource, @NonNull @MaybePresent String[] strings) {
        if (!commandSource.hasPermission("reformcloud.command.reformcloud")) {
            commandSource.sendMessage(TextComponent.of(ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-no-permission")));
            return;
        }

        final String prefix = ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getPrefix() + "§7";

        if (strings.length == 0) {
            commandSource.sendMessage(TextComponent.of(ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-reformcloud-invalid-syntax")));
            commandSource.sendMessage(TextComponent.of(prefix + "/reformcloud copy <name> \n"));
            commandSource.sendMessage(TextComponent.of(prefix + "/reformcloud whitelist <add/remove> <proxyGroup/--all> <name> \n"));
            commandSource.sendMessage(TextComponent.of(prefix + "/reformcloud execute <server/proxy> <name> <command> \n"));
            commandSource.sendMessage(TextComponent.of(prefix + "/reformcloud process <start/stop> <group/name> \n"));
            commandSource.sendMessage(TextComponent.of(prefix + "/reformcloud reload \n"));
            commandSource.sendMessage(TextComponent.of(prefix + "/reformcloud version"));
            return;
        }

        if (!commandSource.hasPermission("reformcloud.command." + strings[0])) {
            commandSource.sendMessage(TextComponent.of(ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-reformcloud-no-permission")));
            return;
        }

        if (strings[0].equalsIgnoreCase("version")) {
            commandSource.sendMessage(TextComponent.of("You are using the ReformCloud V" + StringUtil.REFORM_VERSION + "@" + StringUtil.REFORM_SPECIFICATION));
            return;
        }

        if (strings[0].equalsIgnoreCase("copy")) {
            if (strings.length == 2) {
                ReformCloudAPIVelocity.getInstance().getChannelHandler()
                        .sendPacketAsynchronous("ReformCloudController",
                                new PacketOutDispatchConsoleCommand("copy " + strings[1]));
                commandSource.sendMessage(TextComponent.of(
                        ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-send-controller")
                ));
            } else
                commandSource.sendMessage(TextComponent.of(prefix + "/reformcloud copy <name>"));

            return;
        }

        if (strings[0].equalsIgnoreCase("whitelist")) {
            if (strings.length == 4) {
                StringBuilder stringBuilder = new StringBuilder();
                Arrays.stream(strings).forEach(e -> stringBuilder.append(e).append(StringUtil.SPACE));

                ReformCloudAPIVelocity.getInstance().getChannelHandler()
                        .sendPacketAsynchronous("ReformCloudController",
                                new PacketOutDispatchConsoleCommand(stringBuilder.substring(0, stringBuilder.length() - 1)));
                commandSource.sendMessage(TextComponent.of(
                        ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-send-controller")
                ));
            } else
                commandSource.sendMessage(TextComponent.of("/reformcloud whitelist <add/remove> <proxyGroup/--all> <name>"));

            return;
        }

        if (strings[0].equalsIgnoreCase("execute")) {
            if (strings.length > 3) {
                StringBuilder stringBuilder = new StringBuilder();
                Arrays.stream(strings).forEach(e -> stringBuilder.append(e).append(StringUtil.SPACE));

                ReformCloudAPIVelocity.getInstance().getChannelHandler()
                        .sendPacketAsynchronous("ReformCloudController",
                                new PacketOutDispatchConsoleCommand(stringBuilder.substring(0, stringBuilder.length() - 1)));
                commandSource.sendMessage(TextComponent.of(
                        ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-send-controller")
                ));
            } else
                commandSource.sendMessage(TextComponent.of("/reformcloud execute <server/proxy> <name> <command>"));

            return;
        }

        if (strings[0].equalsIgnoreCase("process")) {
            if (strings.length == 3) {
                StringBuilder stringBuilder = new StringBuilder();
                Arrays.stream(strings).forEach(e -> stringBuilder.append(e).append(StringUtil.SPACE));

                ReformCloudAPIVelocity.getInstance().getChannelHandler()
                        .sendPacketAsynchronous("ReformCloudController",
                                new PacketOutDispatchConsoleCommand(stringBuilder.substring(0, stringBuilder.length() - 1)));
                commandSource.sendMessage(TextComponent.of(
                        ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-send-controller")
                ));
            } else
                commandSource.sendMessage(TextComponent.of("/reformcloud process <start/stop> <group/name>"));

            return;
        }

        if (strings[0].equalsIgnoreCase("reload")) {
            ReformCloudAPIVelocity.getInstance().getChannelHandler()
                    .sendPacketAsynchronous("ReformCloudController",
                            new PacketOutDispatchConsoleCommand("reload"));
            commandSource.sendMessage(TextComponent.of(
                    ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-send-controller")
            ));
            return;
        }

        commandSource.sendMessage(TextComponent.of(ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-command-reformcloud-invalid-syntax")));
        commandSource.sendMessage(TextComponent.of(prefix + "/reformcloud copy <name> \n"));
        commandSource.sendMessage(TextComponent.of(prefix + "/reformcloud whitelist <add/remove> <proxyGroup/--all> <name> \n"));
        commandSource.sendMessage(TextComponent.of(prefix + "/reformcloud execute <server/proxy> <name> <command> \n"));
        commandSource.sendMessage(TextComponent.of(prefix + "/reformcloud process <start/stop> <group/name> \n"));
        commandSource.sendMessage(TextComponent.of(prefix + "/reformcloud reload \n"));
        commandSource.sendMessage(TextComponent.of(prefix + "/reformcloud version"));
    }
}
