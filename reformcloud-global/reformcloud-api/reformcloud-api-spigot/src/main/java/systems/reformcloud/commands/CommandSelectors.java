/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.netty.packets.PacketOutCreateSign;
import systems.reformcloud.netty.packets.PacketOutDeleteSign;
import systems.reformcloud.signaddon.SignSelector;
import systems.reformcloud.signs.Sign;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Set;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 30.12.2018
 */

public final class CommandSelectors implements CommandExecutor, Listener {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player))
            return false;

        if (strings.length == 1 && strings[0].equalsIgnoreCase("help")) {
            commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-usage-1"));
            commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-usage-3"));
            commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-usage-2"));
            return true;
        }

        if (strings.length > 4) {
            commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-usage-1"));
            commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-usage-3"));
            commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-usage-2"));
            return true;
        }

        if (strings.length >= 3 && strings[0].equalsIgnoreCase("selector")) {
            if (strings[1].equalsIgnoreCase("signs")) {
                if (strings[2].equalsIgnoreCase("list")) {
                    if (SignSelector.getInstance() == null) {
                        commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-not-enabled"));
                        return true;
                    }

                    commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-list"));
                    SignSelector.getInstance().getSignMap().values().forEach(e -> commandSender.sendMessage("§7   " + e.getUuid() + " | " + e.getSignPosition().getTargetGroup()));
                    return true;
                } else if (strings[2].equalsIgnoreCase("new") && strings.length == 4) {
                    if (SignSelector.getInstance() == null) {
                        commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-not-enabled"));
                        return true;
                    }

                    if (!ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getServerGroups().containsKey(strings[3])) {
                        commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-create-usage"));
                        return true;
                    }

                    final Block block = ((Player) commandSender).getTargetBlock((Set<Material>) null, 15);
                    if (block.getState() instanceof org.bukkit.block.Sign) {
                        if (SignSelector.getInstance().getSign(block.getLocation()) == null) {
                            ReformCloudAPISpigot.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController", new PacketOutCreateSign(new Sign(UUID.randomUUID(), SignSelector.getInstance().toSignPosition(strings[3], block.getLocation()), null)));
                            commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-create-success"));
                            return true;
                        } else {
                            commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-create-already-exists"));
                            return true;
                        }
                    } else {
                        commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-block-not-sign"));
                        return true;
                    }
                } else if (strings[2].equalsIgnoreCase("remove")) {
                    if (SignSelector.getInstance() == null) {
                        commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-not-enabled"));
                        return true;
                    }

                    final Block block = ((Player) commandSender).getTargetBlock((Set<Material>) null, 15);
                    if (block.getState() instanceof org.bukkit.block.Sign) {
                        final Sign sign = SignSelector.getInstance().getSign(block.getLocation());
                        if (sign != null) {
                            ReformCloudAPISpigot.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController", new PacketOutDeleteSign(sign));
                            commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-delete-success"));
                            return true;
                        } else {
                            commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-delete-not-exists"));
                            return true;
                        }
                    } else {
                        commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-block-not-sign"));
                        return true;
                    }
                } else if (ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getServerGroups().containsKey(strings[2])) {
                    if (SignSelector.getInstance() == null) {
                        commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-not-enabled"));
                        return true;
                    }

                    ((Player) commandSender).getInventory().addItem(signItem(strings[2]));
                    commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-item-success"));
                    return true;
                } else {
                    commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-usage-1"));
                    commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-usage-3"));
                    commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-usage-2"));
                    return true;
                }
            } else {
                commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-usage-1"));
                commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-usage-3"));
                commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-usage-2"));
                return true;
            }
        } else {
            commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-usage-1"));
            commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-usage-3"));
            commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-usage-2"));
            return true;
        }
    }

    @EventHandler
    public void handle(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        if (event.getItem() != null
                && event.getItem().getType().equals(Material.NAME_TAG)
                && event.getItem().hasItemMeta()
                && event.getItem().getItemMeta().getDisplayName().startsWith("§7Create/Delete Sign")
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
                    ReformCloudAPISpigot.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController", new PacketOutCreateSign(new Sign(UUID.randomUUID(), SignSelector.getInstance().toSignPosition(group, block.getLocation()), null)));
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
                    ReformCloudAPISpigot.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController", new PacketOutDeleteSign(sign));
                    player.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-delete-success"));
                } else {
                    player.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-command-signs-delete-not-exists"));
                }
            }
        }
    }

    private ItemStack signItem(final String group) {
        ItemStack itemStack = new ItemStack(Material.NAME_TAG);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§7Create/Delete Sign: " + group);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
