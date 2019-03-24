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
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import net.kyori.text.TextComponent;
import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.bootstrap.VelocityBootstrap;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.packets.*;
import systems.reformcloud.network.query.out.PacketOutQueryGetPermissionHolder;
import systems.reformcloud.network.query.out.PacketOutQueryGetPlayer;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.player.implementations.OnlinePlayer;
import systems.reformcloud.player.permissions.player.PermissionHolder;
import systems.reformcloud.player.version.SpigotVersion;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 03.11.2018
 */

public final class CloudConnectListener {
    @Subscribe(order = PostOrder.FIRST)
    public void handle(final ServerPreConnectEvent event) {
        if (event.getPlayer().getCurrentServer().orElse(null) == null) {
            ProxyInfo proxyInfo = ReformCloudAPIVelocity.getInstance().getProxyInfo();

            final ServerInfo serverInfo = ReformCloudAPIVelocity.getInstance().nextFreeLobby(
                    ReformCloudAPIVelocity.getInstance().getProxyInfo().getProxyGroup(),
                    event.getPlayer()
            );
            if (serverInfo == null)
                event.setResult(ServerPreConnectEvent.ServerResult.denied());

            ReformCloudAPIVelocity.getInstance().getChannelHandler().sendPacketQuerySync(
                    "ReformCloudController",
                    proxyInfo.getCloudProcess().getName(),
                    new PacketOutQueryGetPlayer(
                            event.getPlayer().getUniqueId(),
                            SpigotVersion.getByProtocolId(event.getPlayer().getProtocolVersion().getProtocol()),
                            event.getPlayer().getUsername()
                    ), (configuration, resultID) -> {
                        OfflinePlayer offlinePlayer = configuration.getValue("result", TypeTokenAdaptor.getOFFLINE_PLAYER_TYPE());
                        OnlinePlayer onlinePlayer = new OnlinePlayer(
                                event.getPlayer().getUsername(),
                                event.getPlayer().getUniqueId(),
                                offlinePlayer.getSpigotVersion(),
                                serverInfo.getCloudProcess().getName(),
                                proxyInfo.getCloudProcess().getName()
                        );

                        ReformCloudAPIVelocity.getInstance().getChannelHandler().sendPacketSynchronized("ReformCloudController",
                                new PacketOutUpdateOnlinePlayer(onlinePlayer));

                        if (ReformCloudAPIVelocity.getInstance().getPermissionCache() != null) {
                            ReformCloudAPIVelocity.getInstance().sendPacketQuery("ReformCloudController",
                                    new PacketOutQueryGetPermissionHolder(
                                            new PermissionHolder(offlinePlayer.getUniqueID(), Collections.singletonMap(ReformCloudAPIVelocity
                                                    .getInstance().getPermissionCache().getDefaultGroup().getName(), -1L), new HashMap<>())
                                    ), (configuration1, resultID1) -> {
                                        PermissionHolder permissionHolder = configuration1.getValue("holder", TypeTokenAdaptor.getPERMISSION_HOLDER_TYPE());
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

                                        ReformCloudAPIVelocity.getInstance().getCachedPermissionHolders().put(permissionHolder.getUniqueID(), permissionHolder);
                                    });
                        }

                        event.setResult(ServerPreConnectEvent.ServerResult.allowed(
                                VelocityBootstrap.getInstance().getProxyServer().getServer(serverInfo.getCloudProcess().getName()).get()
                        ));
                    }, (configuration, resultID) -> event.setResult(ServerPreConnectEvent.ServerResult.denied())
            );
        }
    }

    @Subscribe(order = PostOrder.FIRST)
    public void handle(final LoginEvent event) {
        ProxyInfo proxyInfo = ReformCloudAPIVelocity.getInstance().getProxyInfo();

        if (proxyInfo.getProxyGroup().isMaintenance()) {
            if (!ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                    .getProxyGroups().get(proxyInfo.getProxyGroup().getName()).getWhitelist()
                    .contains(event.getPlayer().getUniqueId()) && !event.getPlayer().hasPermission("reformcloud.join.maintenance")) {
                event.setResult(ResultedEvent.ComponentResult.denied(TextComponent.of(
                        ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-maintenance-join-no-permission")
                )));
                return;
            }
        }

        event.setResult(ResultedEvent.ComponentResult.allowed());
        proxyInfo.getOnlinePlayers().add(event.getPlayer().getUniqueId());
        proxyInfo.setOnline(proxyInfo.getOnline() + 1);

        if (proxyInfo.getProxyGroup().getMaxPlayers() <= proxyInfo.getOnline())
            proxyInfo.setFull(true);
        else
            proxyInfo.setFull(false);

        ReformCloudAPIVelocity.getInstance().getChannelHandler().sendPacketSynchronized("ReformCloudController",
                new PacketOutLoginPlayer(event.getPlayer().getUniqueId()),
                new PacketOutProxyInfoUpdate(proxyInfo),
                new PacketOutSendControllerConsoleMessage("Player [Name=" + event.getPlayer().getUsername() + "/UUID="
                        + event.getPlayer().getUniqueId() + "/IP="
                        + event.getPlayer().getRemoteAddress().getAddress().getHostAddress() + "] is now connected"));
    }

    @Subscribe(order = PostOrder.FIRST)
    public void handle(final DisconnectEvent event) {
        ReformCloudAPIVelocity.getInstance().getCachedPermissionHolders().remove(event.getPlayer().getUniqueId());
        ProxyInfo proxyInfo = ReformCloudAPIVelocity.getInstance().getProxyInfo();

        proxyInfo.getOnlinePlayers().remove(event.getPlayer().getUniqueId());

        if (proxyInfo.getProxyGroup().getMaxPlayers() <= VelocityBootstrap.getInstance().getProxyServer().getPlayerCount())
            proxyInfo.setFull(true);
        else
            proxyInfo.setFull(false);

        proxyInfo.setOnline(proxyInfo.getOnline() - 1);
        ReformCloudAPIVelocity.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController",
                new PacketOutLogoutPlayer(event.getPlayer().getUniqueId()), new PacketOutProxyInfoUpdate(proxyInfo),
                new PacketOutSendControllerConsoleMessage("Player [Name=" + event.getPlayer().getUsername() + "/UUID=" +
                        event.getPlayer().getUniqueId() + "/IP=" + event.getPlayer().getRemoteAddress().getAddress().getHostAddress() +
                        "] is now disconnected"));
    }

    @Subscribe(order = PostOrder.FIRST)
    public void handle(final KickedFromServerEvent event) {
        if (!event.kickedDuringLogin()) {
            final ServerInfo serverInfo = ReformCloudAPIVelocity.getInstance().nextFreeLobby(
                    ReformCloudAPIVelocity.getInstance().getProxyInfo().getProxyGroup(),
                    event.getPlayer(),
                    event.getPlayer().getCurrentServer().get().getServerInfo().getName()
            );

            if (serverInfo != null) {
                event.setResult(KickedFromServerEvent.RedirectPlayer.create(
                        VelocityBootstrap.getInstance().getProxyServer().getServer(serverInfo.getCloudProcess().getName()).get()
                ));
            } else {
                event.setResult(KickedFromServerEvent.DisconnectPlayer.create(
                        TextComponent.of(ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-connect-hub-no-server"))
                ));
            }
        }
    }
}
