/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.mobs.SelectorMob;
import systems.reformcloud.mobsaddon.MobSelector;
import systems.reformcloud.utility.map.MapUtility;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class CommandReformMobs implements Serializable, CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player))
            return false;

        Player player = (Player) commandSender;
        if (strings.length >= 5 && strings[0].equalsIgnoreCase("create")) {
            if (MobSelector.getInstance().findMobByName(strings[2]) != null) {
                commandSender.sendMessage("The mob already exists");
                return false;
            }

            if (ReformCloudAPISpigot.getInstance().getServerGroup(strings[3]) == null) {
                commandSender.sendMessage("The serverGroup doesn't exists");
                return false;
            }

            try {
                EntityType entityType = MapUtility.filter(EntityType.values(), e -> e.getEntityClass() != null
                        && e.getEntityClass().getSimpleName().equalsIgnoreCase(strings[1]));
                if (entityType == null || !entityType.isSpawnable()) {
                    commandSender.sendMessage("The mob doesn't exists");
                    return false;
                }

                StringBuilder displayName = new StringBuilder();
                for (int i = 4; i < strings.length; i++)
                    displayName.append(strings[i]).append(" ");

                SelectorMob selectorMob = new SelectorMob(
                        UUID.randomUUID(),
                        entityType.getEntityClass().getSimpleName(),
                        strings[2],
                        ChatColor.translateAlternateColorCodes('&', displayName.substring(0, displayName.length() - 1)),
                        MobSelector.getInstance().toPosition(strings[3], player.getLocation())
                );
                MobSelector.getInstance().createMob(selectorMob);
                commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getPrefix() + "The mob was created successfully");
                return true;
            } catch (final Throwable throwable) {
                commandSender.sendMessage("The mob doesn't exists");
                return false;
            }
        } else if (strings.length == 2 && strings[0].equalsIgnoreCase("deleteall")) {
            int deleted = MobSelector.getInstance().deleteAllMobs(strings[1]);
            commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getPrefix() + deleted + " Mobs deleted");
            return true;
        } else if (strings.length == 2 && strings[0].equalsIgnoreCase("delete")) {
            if (MobSelector.getInstance().deleteMob(strings[1]))
                commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getPrefix() + "Mob has been deleted");
            else
                commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getPrefix() + "Mob doesn't exists");

            return true;
        } else if (strings.length == 1 && strings[0].equalsIgnoreCase("list")) {
            Collection<SelectorMob> mobs = MobSelector.getInstance().getMobs();
            mobs.forEach(e -> commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getPrefix()
                    + "=> " + e.getName() + " " + e.getSelectorMobPosition()));

            return true;
        } else if (strings.length == 1 && strings[0].equalsIgnoreCase("available")) {
            Collection<EntityType> entityTypes = MapUtility.filterAll(EntityType.values(), e -> e.getEntityClass() != null);
            for (EntityType value : entityTypes) {
                if (value.isSpawnable())
                    commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getPrefix() +
                            "=> " + value.name());
            }

            return true;
        } else {
            commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getPrefix()
                    + "§7/mobs create <type> <name> <group> <displayName>");
            commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getPrefix()
                    + "§7/mobs deleteall <group>");
            commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getPrefix()
                    + "§7/mobs delete <name>");
            commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getPrefix()
                    + "§7/mobs list");
            commandSender.sendMessage(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getPrefix()
                    + "§7/mobs available");
        }

        return false;
    }
}
