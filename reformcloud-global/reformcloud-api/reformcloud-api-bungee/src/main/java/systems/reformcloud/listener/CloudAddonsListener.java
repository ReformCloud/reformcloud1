/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.commands.ingame.command.IngameCommand;
import systems.reformcloud.network.packets.PacketOutCommandExecute;
import systems.reformcloud.network.packets.PacketOutUpdatePermissionHolder;
import systems.reformcloud.player.permissions.group.PermissionGroup;
import systems.reformcloud.player.permissions.player.PermissionHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author _Klaro | Pasqual K. / created on 07.11.2018
 */

public final class CloudAddonsListener implements Listener {
    @EventHandler
    public void handle(final ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer)) return;
        final ProxiedPlayer proxiedPlayer = (ProxiedPlayer) event.getSender();

        if (event.isCommand() && !event.isCancelled() && ReformCloudAPIBungee.getInstance().getProxyInfo().getProxyGroup().isControllerCommandLogging())
            ReformCloudAPIBungee.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController", new PacketOutCommandExecute(proxiedPlayer.getName(), proxiedPlayer.getUniqueId(), event.getMessage(), proxiedPlayer.getServer().getInfo().getName()));

        IngameCommand ingameCommand = ReformCloudAPIBungee.getInstance().getIngameCommand(event.getMessage());
        if (ingameCommand != null) {
            ReformCloudAPIBungee.getInstance().executeIngameCommand(
                    ingameCommand,
                    ((ProxiedPlayer) event.getSender()).getUniqueId(),
                    event.getMessage()
            );
        }
    }

    @EventHandler
    public void handle(final PermissionCheckEvent event) {
        if (ReformCloudAPIBungee.getInstance().getPermissionCache() == null)
            return;

        if (event.getSender() instanceof ProxiedPlayer) {
            PermissionHolder permissionHolder = ReformCloudAPIBungee.getInstance()
                    .getCachedPermissionHolders().get(((ProxiedPlayer) event.getSender()).getUniqueId());
            if (permissionHolder == null) {
                event.setHasPermission(false);
                return;
            }

            Map<String, Long> copyOf = new HashMap<>(permissionHolder.getPermissionGroups());

            copyOf.forEach((groupName, timeout) -> {
                if (timeout != -1 && timeout <= System.currentTimeMillis())
                    permissionHolder.getPermissionGroups().remove(groupName);
            });

            if (copyOf.size() != permissionHolder.getPermissionGroups().size()) {
                if (permissionHolder.getPermissionGroups().size() == 0) {
                    permissionHolder.getPermissionGroups().put(
                            ReformCloudAPIBungee.getInstance().getPermissionCache().getDefaultGroup().getName(), -1L
                    );
                }

                ReformCloudAPIBungee.getInstance().getChannelHandler().sendPacketSynchronized(
                        "ReformCloudController", new PacketOutUpdatePermissionHolder(permissionHolder)
                );
            }

            List<PermissionGroup> permissionGroups = ReformCloudAPIBungee.getInstance()
                    .getPermissionCache().getAllRegisteredGroups().stream().filter(e -> permissionHolder
                            .getPermissionGroups().containsKey(e.getName()))
                    .collect(Collectors.toList());
            if (permissionHolder.getPermissionGroups().containsKey(ReformCloudAPIBungee.getInstance()
                    .getPermissionCache().getDefaultGroup().getName())) {
                permissionGroups.add(ReformCloudAPIBungee.getInstance().getPermissionCache().getDefaultGroup());
            }

            event.setHasPermission(permissionHolder.hasPermission(event.getPermission(), permissionGroups));
        } else
            event.setHasPermission(true);
    }
}
