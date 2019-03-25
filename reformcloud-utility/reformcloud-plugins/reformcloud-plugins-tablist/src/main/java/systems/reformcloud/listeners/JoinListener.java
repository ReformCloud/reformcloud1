/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Team;
import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.TablistPlugin;
import systems.reformcloud.player.permissions.group.PermissionGroup;
import systems.reformcloud.player.permissions.player.PermissionHolder;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 25.03.2019
 */

public final class JoinListener implements Serializable, Listener {
    @EventHandler
    public void handle(final PlayerJoinEvent event) {
        if (ReformCloudAPISpigot.getInstance().getPermissionCache() == null)
            return;

        Bukkit.getScheduler().runTaskLater(TablistPlugin.instance, () -> {
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
