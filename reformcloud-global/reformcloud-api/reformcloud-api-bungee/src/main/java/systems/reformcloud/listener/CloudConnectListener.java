/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.listener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.launcher.BungeecordBootstrap;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.proxy.settings.ProxySettings;
import systems.reformcloud.network.packets.PacketOutLoginPlayer;
import systems.reformcloud.network.packets.PacketOutLogoutPlayer;
import systems.reformcloud.network.packets.PacketOutProxyInfoUpdate;
import systems.reformcloud.network.packets.PacketOutSendControllerConsoleMessage;
import systems.reformcloud.network.packets.PacketOutUpdateOnlinePlayer;
import systems.reformcloud.network.packets.PacketOutUpdatePermissionHolder;
import systems.reformcloud.network.query.out.PacketOutQueryGetPermissionHolder;
import systems.reformcloud.network.query.out.PacketOutQueryGetPlayer;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.player.implementations.OnlinePlayer;
import systems.reformcloud.player.permissions.player.PermissionHolder;
import systems.reformcloud.player.version.SpigotVersion;
import systems.reformcloud.utility.TypeTokenAdaptor;

/**
 * @author _Klaro | Pasqual K. / created on 03.11.2018
 */

public final class CloudConnectListener implements Listener {

    private boolean started = false;

    @EventHandler(priority = -127)
    public void handle(final ServerConnectEvent event) {
        if (event.getPlayer().getServer() == null) {
            ProxyInfo proxyInfo = ReformCloudAPIBungee.getInstance().getProxyInfo();

            final ServerInfo serverInfo = ReformCloudAPIBungee.getInstance().nextFreeLobby(
                ReformCloudAPIBungee.getInstance().getProxyInfo().getProxyGroup(),
                event.getPlayer()
            );
            if (serverInfo == null) {
                event.setCancelled(true);
            }

            event.getPlayer().setReconnectServer(BungeecordBootstrap.getInstance().getProxy()
                .getServerInfo(serverInfo.getCloudProcess().getName()));
            ReformCloudAPIBungee.getInstance().getChannelHandler().sendPacketQuerySync(
                "ReformCloudController",
                proxyInfo.getCloudProcess().getName(),
                new PacketOutQueryGetPlayer(
                    event.getPlayer().getUniqueId(),
                    SpigotVersion
                        .getByProtocolId(event.getPlayer().getPendingConnection().getVersion()),
                    event.getPlayer().getName()
                ), (configuration, resultID) -> {
                    OfflinePlayer offlinePlayer = configuration
                        .getValue("result", TypeTokenAdaptor.getOFFLINE_PLAYER_TYPE());
                    OnlinePlayer onlinePlayer = new OnlinePlayer(
                        event.getPlayer().getName(),
                        event.getPlayer().getUniqueId(),
                        offlinePlayer.getSpigotVersion(),
                        serverInfo.getCloudProcess().getName(),
                        proxyInfo.getCloudProcess().getName()
                    );

                    ReformCloudAPIBungee.getInstance().getChannelHandler()
                        .sendPacketSynchronized("ReformCloudController",
                            new PacketOutUpdateOnlinePlayer(onlinePlayer));
                    ReformCloudAPIBungee.getInstance().getOnlinePlayers()
                        .put(event.getPlayer().getUniqueId(), onlinePlayer);

                    if (ReformCloudAPIBungee.getInstance().getPermissionCache() != null) {
                        ReformCloudAPIBungee.getInstance().sendPacketQuery("ReformCloudController",
                            new PacketOutQueryGetPermissionHolder(
                                new PermissionHolder(offlinePlayer.getUniqueID(),
                                    Collections.singletonMap(ReformCloudAPIBungee
                                        .getInstance().getPermissionCache().getDefaultGroup()
                                        .getName(), -1L), new HashMap<>())
                            ), (configuration1, resultID1) -> {
                                PermissionHolder permissionHolder = configuration1
                                    .getValue("holder",
                                        TypeTokenAdaptor.getPERMISSION_HOLDER_TYPE());
                                if (permissionHolder == null) {
                                    return;
                                }

                                Map<String, Long> copyOf = new HashMap<>(
                                    permissionHolder.getPermissionGroups());

                                copyOf.forEach((groupName, timeout) -> {
                                    if (timeout != -1 && timeout <= System.currentTimeMillis()) {
                                        permissionHolder.getPermissionGroups().remove(groupName);
                                    }
                                });

                                if (copyOf.size() != permissionHolder.getPermissionGroups()
                                    .size()) {
                                    if (permissionHolder.getPermissionGroups().size() == 0) {
                                        permissionHolder.getPermissionGroups().put(
                                            ReformCloudAPIBungee.getInstance().getPermissionCache()
                                                .getDefaultGroup().getName(), -1L
                                        );
                                    }

                                    ReformCloudAPIBungee.getInstance().getChannelHandler()
                                        .sendPacketSynchronized(
                                            "ReformCloudController",
                                            new PacketOutUpdatePermissionHolder(permissionHolder)
                                        );
                                }

                                ReformCloudAPIBungee.getInstance().getCachedPermissionHolders()
                                    .put(permissionHolder.getUniqueID(), permissionHolder);
                            });
                    }

                    event.setCancelled(false);
                    event.setTarget(BungeecordBootstrap.getInstance().getProxy()
                        .getServerInfo(serverInfo.getCloudProcess().getName()));
                }, (configuration, resultID) -> event.setCancelled(true)
            );
        }
    }

    @EventHandler(priority = -128)
    public void handle(final LoginEvent event) {
        ProxyInfo proxyInfo = ReformCloudAPIBungee.getInstance().getProxyInfo();

        if (proxyInfo.getProxyGroup().isMaintenance()
            && ProxyServer.getInstance().getPlayer(event.getConnection().getUniqueId()) != null
            && !ProxyServer.getInstance().getPlayer(event.getConnection().getUniqueId())
            .hasPermission("reformcloud.join.maintenance")) {
            event.setCancelReason(TextComponent.fromLegacyText(
                ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                    .getMessage("internal-api-bungee-maintenance-join-no-permission")));
            event.setCancelled(true);
            return;
        } else if (proxyInfo.getProxyGroup().isMaintenance()
            && !ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
            .getProxyGroups().get(proxyInfo.getProxyGroup().getName()).getWhitelist()
            .contains(event.getConnection().getUniqueId())) {
            event.setCancelReason(TextComponent.fromLegacyText(
                ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                    .getMessage("internal-api-bungee-maintenance-join-no-permission")));
            event.setCancelled(true);
            return;
        }

        event.setCancelled(false);
        proxyInfo.getOnlinePlayers().add(event.getConnection().getUniqueId());
        proxyInfo.setOnline(proxyInfo.getOnline() + 1);

        if (proxyInfo.getProxyGroup().getMaxPlayers() <= proxyInfo.getOnline()) {
            proxyInfo.setFull(true);
        } else {
            proxyInfo.setFull(false);
        }

        if (!started && proxyInfo.getProxyGroup().getAutoStart().isEnabled()
            && BungeecordBootstrap.getInstance().getProxy().getOnlineCount() >= proxyInfo
            .getProxyGroup().getAutoStart().getPlayerMax()) {
            started = true;
            ReformCloudAPIBungee.getInstance().startProxy(proxyInfo.getProxyGroup());
            ReformCloudLibraryService.EXECUTOR_SERVICE.execute(() -> {
                ReformCloudLibraryService.sleep(TimeUnit.SECONDS.toMillis(
                    proxyInfo.getProxyGroup().getAutoStart().getAllowAutoStartEverySeconds()));
                started = false;
            });
        }

        ReformCloudAPIBungee.getInstance().getChannelHandler()
            .sendDirectPacket("ReformCloudController",
                new PacketOutLoginPlayer(event.getConnection().getUniqueId()));
        ReformCloudAPIBungee.getInstance().getChannelHandler()
            .sendPacketSynchronized("ReformCloudController",
                new PacketOutProxyInfoUpdate(proxyInfo),
                new PacketOutSendControllerConsoleMessage(
                    "Player §e[Name=" + event.getConnection().getName() + "/UUID="
                        + event.getConnection().getUniqueId() + "/IP="
                        + event.getConnection().getAddress().getAddress().getHostAddress()
                        + "]§r is now §aconnected§r on §e[Proxy=" + proxyInfo.getCloudProcess().getName() + "/Server=" + event.getConnection().getListener().getFallbackServer() + "]§r"));
    }

    @EventHandler(priority = -127)
    public void handle(final ServerSwitchEvent event) {
        ReformCloudLibraryService.EXECUTOR_SERVICE.execute(() -> {
            ReformCloudLibraryService.sleep(TimeUnit.MILLISECONDS, 500);
            OnlinePlayer onlinePlayer = ReformCloudAPIBungee.getInstance()
                .getOnlinePlayer(event.getPlayer().getUniqueId());
            onlinePlayer.setCurrentServer(event.getPlayer().getServer().getInfo().getName());
            ReformCloudAPIBungee.getInstance().updateOnlinePlayer(onlinePlayer);

            initTab(event.getPlayer());
        });
    }

    @EventHandler(priority = -128)
    public void handle(final PlayerDisconnectEvent event) {
        ReformCloudAPIBungee.getInstance().getChannelHandler()
            .sendDirectPacket("ReformCloudController",
                new PacketOutLogoutPlayer(event.getPlayer().getUniqueId()));

        ReformCloudLibraryService.EXECUTOR_SERVICE.execute(() -> {
            ReformCloudLibraryService.sleep(TimeUnit.SECONDS, 1);

            ReformCloudAPIBungee.getInstance().getOnlinePlayers()
                .remove(event.getPlayer().getUniqueId());
            ReformCloudAPIBungee.getInstance().getCachedPermissionHolders()
                .remove(event.getPlayer().getUniqueId());
            ProxyInfo proxyInfo = ReformCloudAPIBungee.getInstance().getProxyInfo();

            proxyInfo.getOnlinePlayers().remove(event.getPlayer().getUniqueId());

            if (proxyInfo.getProxyGroup().getMaxPlayers() <= BungeecordBootstrap.getInstance()
                .getProxy().getPlayers().size()) {
                proxyInfo.setFull(true);
            } else {
                proxyInfo.setFull(false);
            }

            proxyInfo.setOnline(BungeecordBootstrap.getInstance().getProxy().getOnlineCount());
            ReformCloudAPIBungee.getInstance().getChannelHandler().sendDirectPacket(
                "ReformCloudController",
                new PacketOutProxyInfoUpdate(proxyInfo)
            );
            ReformCloudAPIBungee.getInstance().getChannelHandler()
                .sendPacketAsynchronous("ReformCloudController",
                    new PacketOutSendControllerConsoleMessage(
                        "Player §e[Name=" + event.getPlayer().getName() + "/UUID=" +
                            event.getPlayer().getUniqueId() + "/IP=" + event.getPlayer()
                            .getAddress()
                            .getAddress().getHostAddress() +
                            "]§r is now §cdisconnected§r from §e" + proxyInfo.getCloudProcess().getName()));
            BungeecordBootstrap.getInstance().getProxy().getPlayers()
                .forEach(CloudConnectListener::initTab);
            ReformCloudAPIBungee.getInstance().getCachedPermissionHolders()
                .remove(event.getPlayer().getUniqueId());
        });
    }

    @EventHandler(priority = -127)
    public void handle(final ServerKickEvent event) {
        if (event.getCancelServer() != null) {
            final ServerInfo serverInfo = ReformCloudAPIBungee.getInstance().nextFreeLobby(
                ReformCloudAPIBungee.getInstance().getProxyInfo().getProxyGroup(),
                event.getPlayer(),
                event.getPlayer().getServer().getInfo().getName()
            );
            if (serverInfo != null) {
                event.setCancelled(true);
                event.setCancelServer(ProxyServer.getInstance()
                    .getServerInfo(serverInfo.getCloudProcess().getName()));
                BungeecordBootstrap.getInstance().getProxy().getScheduler()
                    .runAsync(BungeecordBootstrap.getInstance(), () -> {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (BaseComponent baseComponent : event.getKickReasonComponent()) {
                            stringBuilder.append(baseComponent.toLegacyText()).append("\n");
                        }

                        event.getPlayer()
                            .sendMessage(ChatMessageType.CHAT, TextComponent.fromLegacyText(
                                ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                                    .getMessage(
                                        "internal-api-bungee-server-kick"
                                    ).replace("%old_server%", event.getKickedFrom().getName())
                                    .replace("%new_server%", serverInfo.getCloudProcess().getName())
                                    .replace("%reason%",
                                        stringBuilder.substring(0, stringBuilder.length() - 2))
                            ));
                    });
            } else {
                event.setCancelled(false);
                event.setCancelServer(null);
                event.setKickReasonComponent(TextComponent.fromLegacyText(
                    ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                        .getMessage("internal-api-bungee-connect-hub-no-server")));
            }
        }
    }

    public static void initTab(final ProxiedPlayer proxiedPlayer) {
        ProxySettings proxySettings = ReformCloudAPIBungee.getInstance().getProxySettings();
        if (proxySettings == null || !proxySettings.isTabEnabled()
            || proxiedPlayer.getServer() == null || proxiedPlayer.getServer().getInfo() == null) {
            return;
        }

        proxiedPlayer.resetTabHeader();
        proxiedPlayer.setTabHeader(TextComponent.fromLegacyText(
            ChatColor.translateAlternateColorCodes('&',
                proxySettings.getTabHeader()
                    .replace("%current_server_group%", ReformCloudAPIBungee.getInstance()
                        .getServerInfo(proxiedPlayer.getServer().getInfo().getName())
                        .getCloudProcess().getGroup())
                    .replace("%current_proxy_group%",
                        ReformCloudAPIBungee.getInstance().getProxyInfo().getCloudProcess()
                            .getGroup())
                    .replace("%current_proxy%",
                        ReformCloudAPIBungee.getInstance().getProxyInfo().getCloudProcess()
                            .getName())
                    .replace("%current_server%", proxiedPlayer.getServer().getInfo().getName())
                    .replace("%online_players_current%", Integer
                        .toString(BungeecordBootstrap.getInstance().getProxy().getOnlineCount()))
                    .replace("%online_players%",
                        Integer.toString(ReformCloudAPIBungee.getInstance().getGlobalOnlineCount()))
                    .replace("%max_players_current%", Integer.toString(
                        ReformCloudAPIBungee.getInstance().getProxyInfo().getProxyGroup()
                            .getMaxPlayers()))
                    .replace("%max_players_global%", Integer
                        .toString(ReformCloudAPIBungee.getInstance().getGlobalMaxOnlineCount()))
            )), TextComponent.fromLegacyText(
            ChatColor.translateAlternateColorCodes('&',
                proxySettings.getTabFooter()
                    .replace("%current_server_group%", ReformCloudAPIBungee.getInstance()
                        .getServerInfo(proxiedPlayer.getServer().getInfo().getName())
                        .getCloudProcess().getGroup())
                    .replace("%current_proxy_group%",
                        ReformCloudAPIBungee.getInstance().getProxyInfo().getCloudProcess()
                            .getGroup())
                    .replace("%current_proxy%",
                        ReformCloudAPIBungee.getInstance().getProxyInfo().getCloudProcess()
                            .getName())
                    .replace("%current_server%", proxiedPlayer.getServer().getInfo().getName())
                    .replace("%online_players_current%", Integer
                        .toString(BungeecordBootstrap.getInstance().getProxy().getOnlineCount()))
                    .replace("%online_players%",
                        Integer.toString(ReformCloudAPIBungee.getInstance().getGlobalOnlineCount()))
                    .replace("%max_players_current%", Integer.toString(
                        ReformCloudAPIBungee.getInstance().getProxyInfo().getProxyGroup()
                            .getMaxPlayers()))
                    .replace("%max_players_global%", Integer
                        .toString(ReformCloudAPIBungee.getInstance().getGlobalMaxOnlineCount()))
            ))
        );
    }
}
