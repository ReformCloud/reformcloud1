/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.network.packets.PacketOutCreateSign;
import systems.reformcloud.network.packets.PacketOutDeleteSign;
import systems.reformcloud.signaddon.SignSelector;
import systems.reformcloud.signs.Sign;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 09.04.2019
 */

public final class CommandReformSigns implements Serializable, Listener, CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (SignSelector.getInstance() == null) {
            commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork()
                    .getMessage("internal-api-spigot-command-signs-not-enabled"));
            return true;
        }

        //list
        if (strings.length == 1 && strings[0].equalsIgnoreCase("list")) {
            commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork()
                    .getMessage("internal-api-spigot-command-signs-list"));
            SignSelector.getInstance().getSignMap().values().forEach(e -> commandSender
                    .sendMessage("§7   " + e.getUuid() + " | " + e.getSignPosition().getTargetGroup()));
            return true;

            //delete
        } else if (strings.length == 1 && strings[0].equalsIgnoreCase("delete")) {
            final Block block = ((Player) commandSender).getTargetBlock(null, 15);
            if (block.getState() instanceof org.bukkit.block.Sign) {
                final Sign sign = SignSelector.getInstance().getSign(block.getLocation());
                if (sign != null) {
                    ReformCloudAPISpigot.getInstance().getChannelHandler().sendDirectPacket("ReformCloudController", new PacketOutDeleteSign(sign));
                    commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork()
                            .getMessage("internal-api-spigot-command-signs-delete-success"));
                    return true;
                } else {
                    commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork()
                            .getMessage("internal-api-spigot-command-signs-delete-not-exists"));
                    return true;
                }
            } else {
                commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork()
                        .getMessage("internal-api-spigot-command-signs-block-not-sign"));
                return true;
            }

            //delete sign with item
        } else if (strings.length == 1 && strings[0].equalsIgnoreCase("deleteitem")) {
            ((Player) commandSender).getInventory().addItem(signItem(null));
            commandSender.sendMessage(ReformCloudAPISpigot.getInstance()
                    .getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-item-success"));
            return true;

            //create
        } else if (strings.length == 2 && strings[0].equalsIgnoreCase("create")) {
            if (ReformCloudAPISpigot.getInstance().getServerGroup(strings[1]) == null) {
                commandSender.sendMessage("ServerGroup not found");
                return true;
            }

            final Block block = ((Player) commandSender).getTargetBlock(null, 15);
            if (block.getState() instanceof org.bukkit.block.Sign) {
                if (SignSelector.getInstance().getSign(block.getLocation()) == null) {
                    ReformCloudAPISpigot.getInstance().getChannelHandler().sendDirectPacket("ReformCloudController", new PacketOutCreateSign(
                            new Sign(UUID.randomUUID(), SignSelector.getInstance()
                                    .toSignPosition(strings[1], block.getLocation()), null)));
                    commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork()
                            .getMessage("internal-api-spigot-command-signs-create-success"));
                    return true;
                } else {
                    commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork()
                            .getMessage("internal-api-spigot-command-signs-create-already-exists"));
                    return true;
                }
            } else {
                commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork()
                        .getMessage("internal-api-spigot-command-signs-block-not-sign"));
                return true;
            }

            //create sign with item
        } else if (strings.length == 2 && strings[0].equalsIgnoreCase("createitem")) {
            if (ReformCloudAPISpigot.getInstance().getServerGroup(strings[1]) == null) {
                commandSender.sendMessage("ServerGroup not found");
                return true;
            }

            ((Player) commandSender).getInventory().addItem(signItem(strings[1]));
            commandSender.sendMessage(ReformCloudAPISpigot.getInstance()
                    .getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-item-success"));
            return true;
        } else if (strings.length == 2 && strings[0].equalsIgnoreCase("deleteall")) {
            int deleted = SignSelector.getInstance().deleteAllSigns(strings[1]);
            commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getPrefix() + " §7" + deleted + " signs deleted");
            return true;
        }

        commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-usage-1"));
        commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-usage-2"));
        commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-usage-3"));
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1
                && (strings[0].equalsIgnoreCase("create")
                || strings[0].equalsIgnoreCase("createitem")
                || strings[0].equalsIgnoreCase("deleteall")))
            return new LinkedList<>(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getServerGroups().keySet());

        return new LinkedList<>(Arrays.asList("create", "createitem", "deleteall", "delete", "deleteitem", "list"));
    }

    private ItemStack signItem(final String group) {
        ItemStack itemStack = new ItemStack(Material.NAME_TAG);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§7Create/Delete Sign: " + (group != null ? group : "none"));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @EventHandler
    public void handle(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        if (event.getItem() != null
                && event.getItem().getType().equals(Material.NAME_TAG)
                && event.getItem().hasItemMeta()
                && event.getItem().getItemMeta().getDisplayName().startsWith("§7Create/Delete Sign")
                && event.getClickedBlock() != null
                && event.getClickedBlock().getState() instanceof org.bukkit.block.Sign) {
            final String group = event.getItem().getItemMeta().getDisplayName().split(":")[1].replaceFirst(" ", "");
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                event.setCancelled(true);
                if (SignSelector.getInstance() == null) {
                    player.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-not-enabled"));
                    return;
                }

                if (!ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getServerGroups().containsKey(group)) {
                    player.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-create-usage"));
                    return;
                }

                final Block block = event.getClickedBlock();
                if (SignSelector.getInstance().getSign(block.getLocation()) == null) {
                    ReformCloudAPISpigot.getInstance().getChannelHandler().sendDirectPacket("ReformCloudController", new PacketOutCreateSign(new Sign(UUID.randomUUID(), SignSelector.getInstance().toSignPosition(group, block.getLocation()), null)));
                    player.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-create-success"));
                } else {
                    player.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-create-already-exists"));
                }

            } else if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                event.setCancelled(true);
                if (SignSelector.getInstance() == null) {
                    player.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-not-enabled"));
                    return;
                }

                final Block block = event.getClickedBlock();
                final Sign sign = SignSelector.getInstance().getSign(block.getLocation());
                if (sign != null && sign.getSignPosition().getTargetGroup().equals(group)) {
                    ReformCloudAPISpigot.getInstance().getChannelHandler().sendDirectPacket("ReformCloudController", new PacketOutDeleteSign(sign));
                    player.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-delete-success"));
                } else {
                    player.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-delete-not-exists"));
                }
            }
        }
    }
}
