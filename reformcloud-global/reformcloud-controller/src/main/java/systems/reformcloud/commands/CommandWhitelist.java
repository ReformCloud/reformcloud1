/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;
import systems.reformcloud.netty.out.PacketOutUpdateAll;
import systems.reformcloud.utility.uuid.UUIDConverter;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 17.12.2018
 */

public final class CommandWhitelist extends Command implements Serializable {
    public CommandWhitelist() {
        super("whitelist", "Adds a player to a proxy whitelist", "reformcloud.command.whitelist", new String[]{"wl"});
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            commandSender.sendMessage("ReformCloud Whitelisted players: ");
            ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().values().forEach(e ->
                    e.getWhitelist().forEach(player -> commandSender.sendMessage("- " + player))
            );
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("add")) {
                UUID uuidInput = getUuidFromString(args[2]);
                if (uuidInput == null) {
                    commandSender.sendMessage("UUID or name not valid");
                    return;
                }

                if (args[1].equalsIgnoreCase("--all")) {
                    ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().values().forEach(group -> {
                        if (!group.getWhitelist().contains(uuidInput)) {
                            ReformCloudController.getInstance().getCloudConfiguration().addPlayerToWhitelist(group.getName(), uuidInput);
                            commandSender.sendMessage("Player " + args[2] + " has been added to " + group.getName() + " whitelist");
                        }
                    });
                    ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
                } else if (ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().containsKey(args[1])) {
                    if (!ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().get(args[1]).getWhitelist().contains(args[2])) {
                        ReformCloudController.getInstance().getCloudConfiguration().addPlayerToWhitelist(args[1], uuidInput);
                        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
                        commandSender.sendMessage("Player " + uuidInput + " has been added to " + args[1] + " whitelist");
                    } else {
                        commandSender.sendMessage("Player is already on the whitelist");
                    }
                } else {
                    commandSender.sendMessage("ProxyGroup isn't registered");
                }
            } else if (args[0].equalsIgnoreCase("remove")) {
                UUID uuidInput = getUuidFromString(args[2]);
                if (uuidInput == null) {
                    commandSender.sendMessage("This is not a UUID.\n");
                    return;
                }
                if (args[1].equalsIgnoreCase("--all")) {
                    ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().values().forEach(group -> {
                        if (group.getWhitelist().contains(uuidInput)) {
                            ReformCloudController.getInstance().getCloudConfiguration().removePlayerFromWhitelist(group.getName(), uuidInput);
                            commandSender.sendMessage("Player " + uuidInput + " has been removed from " + group.getName() + " whitelist");
                        }
                    });
                } else if (ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().containsKey(args[1])) {
                    if (ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().get(args[1]).getWhitelist().contains(args[2])) {
                        ReformCloudController.getInstance().getCloudConfiguration().removePlayerFromWhitelist(args[1], uuidInput);
                        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
                        commandSender.sendMessage("Player " + uuidInput + " has been removed from " + args[1] + " whitelist");
                    } else {
                        commandSender.sendMessage("Player isn't on the whitelist");
                    }
                } else {
                    commandSender.sendMessage("ProxyGroup isn't registered");
                }
            }
        } else {
            commandSender.sendMessage("whitelist add <proxyGroupName/--all> <name/uuid>");
            commandSender.sendMessage("whitelist remove <proxyGroupName/--all> <name/uuid>");
            commandSender.sendMessage("whitelist list");
        }
    }

    private UUID getUuidFromString(String in) {
        try {
            UUID uuidInput;
            if (in.contains("-")) uuidInput = UUID.fromString(in);
            else uuidInput = UUIDConverter.toUUID(in);
            return uuidInput;
        } catch (final IllegalArgumentException ignored) {
            return UUIDConverter.getUUIDFromName(in);
        }
    }
}
