/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.permission.PermissionsSetupEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.PermissionProvider;
import com.velocitypowered.api.permission.PermissionSubject;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import org.checkerframework.checker.optional.qual.MaybePresent;
import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.commands.ingame.command.IngameCommand;
import systems.reformcloud.network.packets.PacketOutCommandExecute;
import systems.reformcloud.network.packets.PacketOutUpdatePermissionHolder;
import systems.reformcloud.player.permissions.group.PermissionGroup;
import systems.reformcloud.player.permissions.player.PermissionHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 07.11.2018
 */

public final class CloudAddonsListener {
    @Subscribe
    public void handle(final PlayerChatEvent event) {
        final Player proxiedPlayer = event.getPlayer();

        if (event.getMessage().startsWith("/") && ReformCloudAPIVelocity.getInstance().getProxyInfo().getProxyGroup().isControllerCommandLogging())
            ReformCloudAPIVelocity.getInstance().getChannelHandler()
                    .sendPacketAsynchronous("ReformCloudController", new PacketOutCommandExecute(
                            proxiedPlayer.getUsername(),
                            proxiedPlayer.getUniqueId(),
                            event.getMessage(),
                            proxiedPlayer.getCurrentServer().get().getServerInfo().getName())
                    );

        IngameCommand ingameCommand = ReformCloudAPIVelocity.getInstance().getIngameCommand(event.getMessage());
        if (ingameCommand != null) {
            ReformCloudAPIVelocity.getInstance().executeIngameCommand(
                    ingameCommand,
                    event.getPlayer().getUniqueId(),
                    event.getMessage()
            );
        }
    }

    @Subscribe
    public void handle(final PermissionsSetupEvent event) {
        if (ReformCloudAPIVelocity.getInstance().getPermissionCache() == null)
            return;

        if (event.getSubject() instanceof Player) {
            PermissionHolder permissionHolder = ReformCloudAPIVelocity.getInstance()
                    .getCachedPermissionHolders().get(((Player) event.getSubject()).getUniqueId());
            if (permissionHolder == null)
                return;

            Map<String, Long> copyOf = new HashMap<>(permissionHolder.getPermissionGroups());

            copyOf.forEach((groupName, timeout) -> {
                if (timeout != -1 && timeout <= System.currentTimeMillis())
                    permissionHolder.getPermissionGroups().remove(groupName);
            });

            if (copyOf.size() != permissionHolder.getPermissionGroups().size()) {
                if (permissionHolder.getPermissionGroups().size() == 0) {
                    permissionHolder.getPermissionGroups().put(
                            ReformCloudAPIVelocity.getInstance().getPermissionCache().getDefaultGroup().getName(), -1L
                    );
                }

                ReformCloudAPIVelocity.getInstance().getChannelHandler().sendPacketSynchronized(
                        "ReformCloudController", new PacketOutUpdatePermissionHolder(permissionHolder)
                );
            }

            event.setProvider(new PlayerPermissionProvider(((Player) event.getSubject()), permissionHolder));
        }
    }

    private class PlayerPermissionProvider implements PermissionProvider, PermissionFunction {
        private List<PermissionGroup> permissionGroups;
        private Player player;
        private PermissionHolder permissionHolder;

        public PlayerPermissionProvider(Player player, PermissionHolder permissionHolder) {
            this.permissionGroups = permissionHolder.getAllPermissionGroups(ReformCloudAPIVelocity.getInstance().getPermissionCache());
            this.permissionHolder = permissionHolder;
            this.player = player;
        }

        @Override
        public @MaybePresent Tristate getPermissionValue(@MaybePresent String s) {
            return Tristate.fromNullableBoolean(permissionHolder.hasPermission(s, permissionGroups));
        }

        @Override
        public @MaybePresent PermissionFunction createFunction(@MaybePresent PermissionSubject permissionSubject) {
            if (this.player != permissionSubject)
                throw new IllegalStateException("CreateFunction called with different argument");

            return this;
        }
    }
}
