/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import systems.reformcloud.ReformAddonChat;
import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.player.permissions.group.PermissionGroup;
import systems.reformcloud.player.permissions.player.PermissionHolder;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 25.03.2019
 */

public final class ChatListener implements Serializable, Listener {
    @EventHandler
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
                            ReformAddonChat.getPlugin(ReformAddonChat.class).getConfig().getString("format")
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
}
