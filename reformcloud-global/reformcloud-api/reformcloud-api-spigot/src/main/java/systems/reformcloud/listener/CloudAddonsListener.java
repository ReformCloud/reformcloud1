/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Team;
import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.launcher.SpigotBootstrap;
import systems.reformcloud.player.permissions.group.PermissionGroup;
import systems.reformcloud.player.permissions.player.PermissionHolder;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 19.04.2019
 */

public final class CloudAddonsListener implements Serializable, Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handle(final AsyncPlayerChatEvent event) {
        if (!event.isCancelled() && ReformCloudAPISpigot.getInstance().getPermissionCache() != null) {
            PermissionHolder permissionHolder = ReformCloudAPISpigot.getInstance().getCachedPermissionHolders().get(event.getPlayer().getUniqueId());
            if (permissionHolder == null)
                return;

            PermissionGroup permissionGroup = permissionHolder.getHighestPlayerGroup(ReformCloudAPISpigot.getInstance().getPermissionCache()).orElse(null);
            if (permissionGroup == null)
                return;

            event.setFormat(
                    ChatColor.translateAlternateColorCodes('&',
                            ReformCloudAPISpigot.getInstance().getPermissionCache().getChatFormat()
                                    .replace("%group%", permissionGroup.getName())
                                    .replace("%player%", event.getPlayer().getName())
                                    .replace("%prefix%", ChatColor.translateAlternateColorCodes('&', permissionGroup.getPrefix()))
                                    .replace("%suffix%", ChatColor.translateAlternateColorCodes('&', permissionGroup.getSuffix()))
                                    .replace("%display%", ChatColor.translateAlternateColorCodes('&', permissionGroup.getDisplay()))
                                    .replace("%id%", Integer.toString(permissionGroup.getGroupID()))
                                    .replace("%message%", event.getPlayer().hasPermission("chat.color") ?
                                            ChatColor.translateAlternateColorCodes('&', event.getMessage().replace("%", "%%"))
                                            :
                                            ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', event.getMessage().replace("%", "%%"))))
                    )
            );
        }
    }

    @EventHandler
    public void handle(final PlayerJoinEvent event) {
        if (ReformCloudAPISpigot.getInstance().getPermissionCache() == null)
            return;

        Bukkit.getScheduler().runTaskLater(SpigotBootstrap.getInstance(), () -> {
            PermissionHolder permissionHolder = ReformCloudAPISpigot.getInstance().getCachedPermissionHolders().get(event.getPlayer().getUniqueId());
            if (permissionHolder == null)
                return;

            PermissionGroup permissionGroup = permissionHolder.getHighestPlayerGroup(ReformCloudAPISpigot.getInstance().getPermissionCache()).orElse(null);
            if (permissionGroup == null)
                return;

            Player player = event.getPlayer();
            if (player.getScoreboard() == null)
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

            Bukkit.getOnlinePlayers().forEach(online -> {
                if (online.getScoreboard() == null)
                    online.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

                String teamName = permissionGroup.getGroupID() + permissionGroup.getName();

                Team team = online.getScoreboard().getTeam(teamName);
                if (team == null)
                    team = online.getScoreboard().registerNewTeam(teamName);

                team.addEntry(player.getName());

                team.setPrefix(ChatColor.translateAlternateColorCodes('&', permissionGroup.getPrefix()));
                team.setSuffix(ChatColor.translateAlternateColorCodes('&', permissionGroup.getSuffix()));
                team.setDisplayName(ChatColor.translateAlternateColorCodes('&', permissionGroup.getDisplay()));
                if (permissionGroup.getTabColorCode() != null && permissionGroup.getTabColorCode().trim().length() == 1)
                    team.setColor(ChatColor.getByChar(permissionGroup.getTabColorCode()));

                PermissionHolder onlinePermsHolder = ReformCloudAPISpigot.getInstance().getCachedPermissionHolders().get(online.getUniqueId());
                if (onlinePermsHolder == null)
                    return;

                PermissionGroup onlinePermissionGroup = onlinePermsHolder.getHighestPlayerGroup(ReformCloudAPISpigot.getInstance().getPermissionCache()).orElse(null);
                if (onlinePermissionGroup == null)
                    return;

                String onlineTeamName = onlinePermissionGroup.getGroupID() + onlinePermissionGroup.getName();

                Team onlineTeam = online.getScoreboard().getTeam(onlineTeamName);
                if (onlineTeam == null)
                    onlineTeam = online.getScoreboard().registerNewTeam(onlineTeamName);

                onlineTeam.addEntry(online.getName());

                onlineTeam.setPrefix(ChatColor.translateAlternateColorCodes('&', onlinePermissionGroup.getPrefix()));
                onlineTeam.setSuffix(ChatColor.translateAlternateColorCodes('&', onlinePermissionGroup.getSuffix()));
                online.setDisplayName(ChatColor.translateAlternateColorCodes('&', onlinePermissionGroup.getDisplay()));
            });
        }, 3L);
    }
}
