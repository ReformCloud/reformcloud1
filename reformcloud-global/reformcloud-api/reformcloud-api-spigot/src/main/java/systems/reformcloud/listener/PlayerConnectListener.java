/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.launcher.SpigotBootstrap;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.packets.PacketOutCheckPlayer;
import systems.reformcloud.network.packets.PacketOutServerInfoUpdate;
import systems.reformcloud.network.query.out.PacketOutQueryGetPermissionHolder;
import systems.reformcloud.permissions.ReflectionUtil;
import systems.reformcloud.permissions.permissible.Permissible;
import systems.reformcloud.player.permissions.player.PermissionHolder;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public final class PlayerConnectListener implements Listener, Serializable {

    private boolean started = false;

    @EventHandler(priority = EventPriority.LOW)
    public void handle(final AsyncPlayerPreLoginEvent event) {
        if (!ReformCloudAPISpigot.getInstance().getServerInfo().getServerState().isJoineable()) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, "Server is not ready, yet");
            return;
        }

        event.allow();
    }

    @EventHandler
    public void handle(final PlayerLoginEvent event) {
        boolean ok = ReformCloudAPISpigot.getInstance().getChannelHandler().sendPacketQuerySync(
            "ReformCloudController",
            ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess().getName(),
            new PacketOutCheckPlayer(event.getPlayer().getUniqueId())
        ).sendOnCurrentThread("ReformCloudController")
            .syncUninterruptedly(500, TimeUnit.MILLISECONDS).getConfiguration()
            .getBooleanValue("checked");
        if (!ok) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED,
                ReformCloudAPISpigot.getInstance().getInternalCloudNetwork()
                    .getMessage("internal-api-spigot-connect-only-proxy"));
            return;
        }

        if (ReformCloudAPISpigot.getInstance().getPermissionCache() != null) {
            ReformCloudAPISpigot.getInstance().sendPacketQuery(
                "ReformCloudController",
                new PacketOutQueryGetPermissionHolder(
                    new PermissionHolder(event.getPlayer().getUniqueId(), Collections.singletonMap(
                        ReformCloudAPISpigot.getInstance().getPermissionCache().getDefaultGroup()
                            .getName(), -1L
                    ), new HashMap<>())
                ), (configuration, resultID) -> {
                    PermissionHolder permissionHolder = configuration
                        .getValue("holder", TypeTokenAdaptor.getPERMISSION_HOLDER_TYPE());
                    if (permissionHolder == null) {
                        return;
                    }

                    ReformCloudAPISpigot.getInstance().getCachedPermissionHolders()
                        .put(event.getPlayer().getUniqueId(), permissionHolder);

                    Field field;

                    try {
                        Class<?> clazz = ReflectionUtil.reflectClazz(".entity.CraftHumanEntity");

                        if (clazz != null) {
                            field = clazz.getDeclaredField("perm");
                        } else {
                            field = Class.forName("net.glowstone.entity.GlowHumanEntity")
                                .getDeclaredField("permissions");
                        }

                        field.setAccessible(true);
                        field.set(event.getPlayer(),
                            new Permissible(event.getPlayer(), permissionHolder));
                    } catch (final NoSuchFieldException | IllegalAccessException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }

                    SpigotBootstrap.getInstance().getServer().getScheduler()
                        .runTask(SpigotBootstrap.getInstance(), () -> {
                            if (ReformCloudAPISpigot.getInstance().getServerInfo().getServerGroup()
                                .isMaintenance()
                                && !event.getPlayer()
                                .hasPermission("reformcloud.join.server.maintenance")) {
                                event.getPlayer().kickPlayer(
                                    ReformCloudAPISpigot.getInstance().getInternalCloudNetwork()
                                        .getMessage("internal-api-spigot-connect-no-permission")
                                );
                                return;
                            }

                            if (ReformCloudAPISpigot.getInstance().getServerInfo().getServerGroup()
                                .getJoinPermission() != null
                                && !event.getPlayer().hasPermission(
                                ReformCloudAPISpigot.getInstance().getServerInfo().getServerGroup()
                                    .getJoinPermission())) {
                                event.getPlayer().kickPlayer(
                                    ReformCloudAPISpigot.getInstance().getInternalCloudNetwork()
                                        .getMessage("internal-api-spigot-connect-no-permission")
                                );
                            }
                        });

                    event.allow();
                }
            );
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handle(final PlayerJoinEvent event) {
        final ServerInfo serverInfo = ReformCloudAPISpigot.getInstance().getServerInfo();

        if (serverInfo.getOnlinePlayers().contains(event.getPlayer().getUniqueId())) {
            event.getPlayer().kickPlayer("§cYou're already connected to this server");
            return;
        }

        serverInfo.getOnlinePlayers().add(event.getPlayer().getUniqueId());
        serverInfo.setOnline(serverInfo.getOnline() + 1);

        if (serverInfo.getOnline() >= serverInfo.getServerGroup().getMaxPlayers()) {
            serverInfo.setFull(true);
        } else {
            serverInfo.setFull(false);
        }

        ReformCloudAPISpigot.getInstance().getChannelHandler()
            .sendPacketSynchronized("ReformCloudController",
                new PacketOutServerInfoUpdate(serverInfo));
        if (!started && serverInfo.getServerGroup().getAutoStart().isEnabled()
            && Bukkit.getServer().getOnlinePlayers().size() >= serverInfo.getServerGroup()
            .getAutoStart().getPlayerMax()) {
            started = true;
            ReformCloudAPISpigot.getInstance().startQueuedProcess(serverInfo.getServerGroup());
            SpigotBootstrap.getInstance().getServer().getScheduler()
                .runTaskLaterAsynchronously(SpigotBootstrap.getInstance(), () -> {
                    started = false;
                }, TimeUnit.SECONDS.toMillis(
                    serverInfo.getServerGroup().getAutoStart().getAllowAutoStartEverySeconds()));
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void handle(final PlayerQuitEvent event) {
        ReformCloudAPISpigot.getInstance().getCachedPermissionHolders()
            .remove(event.getPlayer().getUniqueId());

        SpigotBootstrap.getInstance().getServer().getScheduler()
            .runTaskLaterAsynchronously(SpigotBootstrap.getInstance(), () -> {
                final ServerInfo serverInfo = ReformCloudAPISpigot.getInstance().getServerInfo();
                serverInfo.getOnlinePlayers().remove(event.getPlayer().getUniqueId());

                serverInfo.setOnline(serverInfo.getOnline() - 1);

                if (serverInfo.getOnline() >= serverInfo.getServerGroup().getMaxPlayers()) {
                    serverInfo.setFull(true);
                } else {
                    serverInfo.setFull(false);
                }

                ReformCloudAPISpigot.getInstance().getChannelHandler()
                    .sendPacketSynchronized("ReformCloudController",
                        new PacketOutServerInfoUpdate(serverInfo));
            }, 20L);

        ReformCloudAPISpigot.getInstance().updateTempStats();
    }
}
