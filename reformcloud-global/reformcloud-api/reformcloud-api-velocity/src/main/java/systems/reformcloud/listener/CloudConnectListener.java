/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.bootstrap.VelocityBootstrap;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.proxy.settings.ProxySettings;
import systems.reformcloud.network.packets.*;
import systems.reformcloud.network.query.out.PacketOutQueryGetPlayer;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.player.implementations.OnlinePlayer;
import systems.reformcloud.player.version.SpigotVersion;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.util.concurrent.TimeUnit;

/**
 * @author _Klaro | Pasqual K. / created on 03.11.2018
 */

public final class CloudConnectListener {

    private boolean started = false;

    @Subscribe(order = PostOrder.FIRST)
    public void handle(final ServerPreConnectEvent event) {
        if (event.getPlayer().getCurrentServer().orElse(null) == null) {
            ProxyInfo proxyInfo = ReformCloudAPIVelocity.getInstance().getProxyInfo();

            final ServerInfo serverInfo = ReformCloudAPIVelocity.getInstance().nextFreeLobby(
                ReformCloudAPIVelocity.getInstance().getProxyInfo().getProxyGroup(),
                event.getPlayer()
            );
            if (serverInfo == null) {
                event.setResult(ServerPreConnectEvent.ServerResult.denied());
            }

            ReformCloudAPIVelocity.getInstance().getChannelHandler().sendPacketQuerySync(
                "ReformCloudController",
                proxyInfo.getCloudProcess().getName(),
                new PacketOutQueryGetPlayer(
                    event.getPlayer().getUniqueId(),
                    SpigotVersion
                        .getByProtocolId(event.getPlayer().getProtocolVersion().getProtocol()),
                    event.getPlayer().getUsername()
                ), (configuration, resultID) -> {
                    OfflinePlayer offlinePlayer = configuration
                        .getValue("result", TypeTokenAdaptor.getOFFLINE_PLAYER_TYPE());
                    OnlinePlayer onlinePlayer = new OnlinePlayer(
                        event.getPlayer().getUsername(),
                        event.getPlayer().getUniqueId(),
                        offlinePlayer.getSpigotVersion(),
                        serverInfo.getCloudProcess().getName(),
                        proxyInfo.getCloudProcess().getName()
                    );

                    ReformCloudAPIVelocity.getInstance().getOnlinePlayers()
                        .put(event.getPlayer().getUniqueId(), onlinePlayer);
                    ReformCloudAPIVelocity.getInstance().getChannelHandler()
                        .sendPacketSynchronized("ReformCloudController",
                            new PacketOutUpdateOnlinePlayer(onlinePlayer));

                    event.setResult(ServerPreConnectEvent.ServerResult.allowed(
                        VelocityBootstrap.getInstance().getProxyServer()
                            .getServer(serverInfo.getCloudProcess().getName()).get()
                    ));
                }, (configuration, resultID) -> event
                    .setResult(ServerPreConnectEvent.ServerResult.denied())
            );
        }
    }

    @Subscribe(order = PostOrder.FIRST)
    public void handle(final LoginEvent event) {
        ProxyInfo proxyInfo = ReformCloudAPIVelocity.getInstance().getProxyInfo();

        if (proxyInfo.getProxyGroup().isMaintenance()) {
            if (!ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                .getProxyGroups().get(proxyInfo.getProxyGroup().getName()).getWhitelist()
                .contains(event.getPlayer().getUniqueId()) && !event.getPlayer()
                .hasPermission("reformcloud.join.maintenance")) {
                event.setResult(ResultedEvent.ComponentResult.denied(TextComponent.of(
                    ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                        .getMessage("internal-api-bungee-maintenance-join-no-permission")
                )));
                return;
            }
        }

        event.setResult(ResultedEvent.ComponentResult.allowed());
        proxyInfo.getOnlinePlayers().add(event.getPlayer().getUniqueId());
        proxyInfo.setOnline(proxyInfo.getOnline() + 1);

        if (proxyInfo.getProxyGroup().getMaxPlayers() <= proxyInfo.getOnline()) {
            proxyInfo.setFull(true);
        } else {
            proxyInfo.setFull(false);
        }

        if (!started && proxyInfo.getProxyGroup().getAutoStart().isEnabled()
            && VelocityBootstrap.getInstance().getProxy().getPlayerCount() >= proxyInfo
            .getProxyGroup().getAutoStart().getPlayerMax()) {
            started = true;
            ReformCloudAPIVelocity.getInstance().startProxy(proxyInfo.getProxyGroup());
            ReformCloudLibraryService.EXECUTOR_SERVICE.execute(() -> {
                ReformCloudLibraryService.sleep(TimeUnit.SECONDS,
                    proxyInfo.getProxyGroup().getAutoStart().getAllowAutoStartEverySeconds());
                started = false;
            });
        }

        ReformCloudAPIVelocity.getInstance().getChannelHandler()
            .sendDirectPacket("ReformCloudController",
                new PacketOutLoginPlayer(event.getPlayer().getUniqueId()));
        ReformCloudAPIVelocity.getInstance().getChannelHandler()
            .sendPacketSynchronized("ReformCloudController",
                new PacketOutProxyInfoUpdate(proxyInfo),
                new PacketOutSendControllerConsoleMessage(
                    "Player [Name=" + event.getPlayer().getUsername() + "/UUID="
                        + event.getPlayer().getUniqueId() + "/IP="
                        + event.getPlayer().getRemoteAddress().getAddress().getHostAddress()
                        + "] is now connected"));
    }

    @Subscribe(order = PostOrder.FIRST)
    public void handle(final ServerConnectedEvent event) {
        VelocityBootstrap.getInstance().getProxy().getScheduler()
            .buildTask(VelocityBootstrap.getInstance(), () -> {
                OnlinePlayer onlinePlayer = ReformCloudAPIVelocity.getInstance()
                    .getOnlinePlayer(event.getPlayer().getUniqueId());
                onlinePlayer.setCurrentServer(event.getServer().getServerInfo().getName());
                ReformCloudAPIVelocity.getInstance().updateOnlinePlayer(onlinePlayer);

                initTab(event.getPlayer());
            }).delay(500, TimeUnit.MILLISECONDS).schedule();
    }

    @Subscribe(order = PostOrder.FIRST)
    public void handle(final DisconnectEvent event) {
        ReformCloudAPIVelocity.getInstance().getCachedPermissionHolders()
            .remove(event.getPlayer().getUniqueId());
        ProxyInfo proxyInfo = ReformCloudAPIVelocity.getInstance().getProxyInfo();

        proxyInfo.getOnlinePlayers().remove(event.getPlayer().getUniqueId());

        if (proxyInfo.getProxyGroup().getMaxPlayers() <= VelocityBootstrap.getInstance()
            .getProxyServer().getPlayerCount()) {
            proxyInfo.setFull(true);
        } else {
            proxyInfo.setFull(false);
        }

        proxyInfo.setOnline(proxyInfo.getOnline() - 1);
        ReformCloudAPIVelocity.getInstance().getCachedPermissionHolders()
            .remove(event.getPlayer().getUniqueId());

        ReformCloudAPIVelocity.getInstance().getOnlinePlayers()
            .remove(event.getPlayer().getUniqueId());
        ReformCloudAPIVelocity.getInstance().getChannelHandler()
            .sendDirectPacket("ReformCloudController",
                new PacketOutLogoutPlayer(event.getPlayer().getUniqueId()));
        ReformCloudAPIVelocity.getInstance().getChannelHandler()
            .sendPacketAsynchronous("ReformCloudController",
                new PacketOutProxyInfoUpdate(proxyInfo),
                new PacketOutSendControllerConsoleMessage(
                    "Player [Name=" + event.getPlayer().getUsername() + "/UUID=" +
                        event.getPlayer().getUniqueId() + "/IP=" + event.getPlayer()
                        .getRemoteAddress().getAddress().getHostAddress() +
                        "] is now disconnected"));
        VelocityBootstrap.getInstance().getProxy().getAllPlayers()
            .forEach(CloudConnectListener::initTab);
    }

    @Subscribe(order = PostOrder.FIRST)
    public void handle(final KickedFromServerEvent event) {
        if (!event.kickedDuringServerConnect()) {
            final ServerInfo serverInfo = ReformCloudAPIVelocity.getInstance().nextFreeLobby(
                ReformCloudAPIVelocity.getInstance().getProxyInfo().getProxyGroup(),
                event.getPlayer(),
                event.getPlayer().getCurrentServer().get().getServerInfo().getName()
            );

            if (serverInfo != null) {
                event.setResult(KickedFromServerEvent.RedirectPlayer.create(
                    VelocityBootstrap.getInstance().getProxyServer()
                        .getServer(serverInfo.getCloudProcess().getName()).get()
                ));

                VelocityBootstrap.getInstance().getProxy().getScheduler()
                    .buildTask(VelocityBootstrap.getInstance(), () -> {
                        event.getPlayer()
                            .sendMessage(TextComponent.of(
                                ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                                    .getMessage(
                                        "internal-api-bungee-server-kick"
                                    ).replace("%old_server%",
                                    event.getServer().getServerInfo().getName())
                                    .replace("%new_server%", serverInfo.getCloudProcess().getName())
                                    .replace("%reason%", event.getOriginalReason().isPresent()
                                        && event.getOriginalReason().get().insertion() != null
                                        ? event.getOriginalReason().get().insertion()
                                        : "§cReason undefined")
                            ));
                    }).schedule();
            } else {
                event.setResult(KickedFromServerEvent.DisconnectPlayer.create(
                    TextComponent.of(ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                        .getMessage("internal-api-bungee-connect-hub-no-server"))
                ));
            }
        }
    }

    public static void initTab(final Player proxiedPlayer) {
        ProxySettings proxySettings = ReformCloudAPIVelocity.getInstance().getProxySettings();
        if (proxySettings == null || !proxySettings.isTabEnabled() || !proxiedPlayer
            .getCurrentServer().isPresent()) {
            return;
        }

        proxiedPlayer.getTabList().clearHeaderAndFooter();
        proxiedPlayer.getTabList().setHeaderAndFooter(TextComponent.of(
            CloudAddonsListener.translateAlternateColorCodes('&',
                proxySettings.getTabHeader()
                    .replace("%current_server_group%", ReformCloudAPIVelocity.getInstance()
                        .getServerInfo(
                            proxiedPlayer.getCurrentServer().get().getServerInfo().getName())
                        .getCloudProcess().getGroup())
                    .replace("%current_proxy_group%",
                        ReformCloudAPIVelocity.getInstance().getProxyInfo().getCloudProcess()
                            .getGroup())
                    .replace("%current_proxy%",
                        ReformCloudAPIVelocity.getInstance().getProxyInfo().getCloudProcess()
                            .getName())
                    .replace("%current_server%",
                        proxiedPlayer.getCurrentServer().get().getServerInfo().getName())
                    .replace("%online_players_current%", Integer
                        .toString(VelocityBootstrap.getInstance().getProxy().getPlayerCount()))
                    .replace("%online_players%", Integer
                        .toString(ReformCloudAPIVelocity.getInstance().getGlobalOnlineCount()))
                    .replace("%max_players_current%", Integer.toString(
                        ReformCloudAPIVelocity.getInstance().getProxyInfo().getProxyGroup()
                            .getMaxPlayers()))
                    .replace("%max_players_global%", Integer
                        .toString(ReformCloudAPIVelocity.getInstance().getGlobalMaxOnlineCount()))
            )), TextComponent.of(
            CloudAddonsListener.translateAlternateColorCodes('&',
                proxySettings.getTabFooter()
                    .replace("%current_server_group%", ReformCloudAPIVelocity.getInstance()
                        .getServerInfo(
                            proxiedPlayer.getCurrentServer().get().getServerInfo().getName())
                        .getCloudProcess().getGroup())
                    .replace("%current_proxy_group%",
                        ReformCloudAPIVelocity.getInstance().getProxyInfo().getCloudProcess()
                            .getGroup())
                    .replace("%current_proxy%",
                        ReformCloudAPIVelocity.getInstance().getProxyInfo().getCloudProcess()
                            .getName())
                    .replace("%current_server%",
                        proxiedPlayer.getCurrentServer().get().getServerInfo().getName())
                    .replace("%online_players_current%", Integer
                        .toString(VelocityBootstrap.getInstance().getProxy().getPlayerCount()))
                    .replace("%online_players%", Integer
                        .toString(ReformCloudAPIVelocity.getInstance().getGlobalOnlineCount()))
                    .replace("%max_players_current%", Integer.toString(
                        ReformCloudAPIVelocity.getInstance().getProxyInfo().getProxyGroup()
                            .getMaxPlayers()))
                    .replace("%max_players_global%", Integer
                        .toString(ReformCloudAPIVelocity.getInstance().getGlobalMaxOnlineCount()))
            ))
        );
    }
}
