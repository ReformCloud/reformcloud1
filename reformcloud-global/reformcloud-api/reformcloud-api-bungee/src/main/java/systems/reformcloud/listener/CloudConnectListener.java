/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.listener;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.launcher.BungeecordBootstrap;
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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author _Klaro | Pasqual K. / created on 03.11.2018
 */

public final class CloudConnectListener implements Listener {
    @EventHandler(priority = - 127)
    public void handle(final ServerConnectEvent event) {
        if (event.getPlayer().getServer() == null) {
            ProxyInfo proxyInfo = ReformCloudAPIBungee.getInstance().getProxyInfo();

            final ServerInfo serverInfo = ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getServerProcessManager().nextFreeLobby(event.getPlayer().getPermissions());
            if (serverInfo == null)
                event.setCancelled(true);

            ReformCloudAPIBungee.getInstance().getChannelHandler().sendPacketQuerySync(
                    "ReformCloudController",
                    proxyInfo.getCloudProcess().getName(),
                    new PacketOutQueryGetPlayer(
                            event.getPlayer().getUniqueId(),
                            SpigotVersion.getByProtocolId(event.getPlayer().getPendingConnection().getVersion()),
                            event.getPlayer().getName()
                    ), (configuration, resultID) -> {
                        OfflinePlayer offlinePlayer = configuration.getValue("result", TypeTokenAdaptor.getOFFLINE_PLAYER_TYPE());
                        OnlinePlayer onlinePlayer = new OnlinePlayer(
                                event.getPlayer().getName(),
                                event.getPlayer().getUniqueId(),
                                offlinePlayer.getSpigotVersion(),
                                serverInfo.getCloudProcess().getName(),
                                proxyInfo.getCloudProcess().getName()
                        );

                        ReformCloudAPIBungee.getInstance().getChannelHandler().sendPacketSynchronized("ReformCloudController",
                                new PacketOutUpdateOnlinePlayer(onlinePlayer));

                        ReformCloudAPIBungee.getInstance().sendPacketQuery("ReformCloudController",
                                new PacketOutQueryGetPermissionHolder(
                                        new PermissionHolder(offlinePlayer, new ArrayList<>(), new HashMap<>())
                                ), (configuration1, resultID1) -> {
                                    PermissionHolder permissionHolder = configuration1.getValue("holder", TypeTokenAdaptor.getPERMISSION_HOLDER_TYPE());
                                    ReformCloudAPIBungee.getInstance().getCachedPermissionHolders().put(permissionHolder.getOfflinePlayer().getUniqueID(), permissionHolder);
                                });

                        event.setCancelled(false);
                        event.setTarget(BungeecordBootstrap.getInstance().getProxy().getServerInfo(serverInfo.getCloudProcess().getName()));
                    }, (configuration, resultID) -> event.setCancelled(true)
            );
        }
    }

    @EventHandler(priority = - 128)
    public void handle(final LoginEvent event) {
        ProxyInfo proxyInfo = ReformCloudAPIBungee.getInstance().getProxyInfo();

        if (proxyInfo.getProxyGroup().isMaintenance()
                && ProxyServer.getInstance().getPlayer(event.getConnection().getUniqueId()) != null
                && !ProxyServer.getInstance().getPlayer(event.getConnection().getUniqueId()).hasPermission("reformcloud.join.maintenance")) {
            event.setCancelReason(TextComponent.fromLegacyText(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-maintenance-join-no-permission")));
            event.setCancelled(true);
            return;
        } else if (proxyInfo.getProxyGroup().isMaintenance()
                && ! ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                .getProxyGroups().get(proxyInfo.getProxyGroup().getName()).getWhitelist()
                .contains(event.getConnection().getUniqueId())) {
            event.setCancelReason(TextComponent.fromLegacyText(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-maintenance-join-no-permission")));
            event.setCancelled(true);
            return;
        }

        event.setCancelled(false);
        proxyInfo.getOnlinePlayers().add(event.getConnection().getUniqueId());
        proxyInfo.setOnline(proxyInfo.getOnline() + 1);

        if (proxyInfo.getProxyGroup().getMaxPlayers() <= proxyInfo.getOnline())
            proxyInfo.setFull(true);
        else
            proxyInfo.setFull(false);

        ReformCloudAPIBungee.getInstance().getChannelHandler().sendPacketSynchronized("ReformCloudController", new PacketOutLoginPlayer(event.getConnection().getUniqueId()), new PacketOutProxyInfoUpdate(proxyInfo), new PacketOutSendControllerConsoleMessage("Player [Name=" + event.getConnection().getName() + "/UUID=" + event.getConnection().getUniqueId() + "/IP=" + event.getConnection().getAddress().getAddress().getHostAddress() + "] is now connected"));
    }

    @EventHandler(priority = - 128)
    public void handle(final PlayerDisconnectEvent event) {
        ProxyInfo proxyInfo = ReformCloudAPIBungee.getInstance().getProxyInfo();

        proxyInfo.getOnlinePlayers().remove(event.getPlayer().getUniqueId());

        if (proxyInfo.getProxyGroup().getMaxPlayers() <= BungeecordBootstrap.getInstance().getProxy().getPlayers().size())
            proxyInfo.setFull(true);
        else
            proxyInfo.setFull(false);

        proxyInfo.setOnline(proxyInfo.getOnline() - 1);
        ReformCloudAPIBungee.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController", new PacketOutLogoutPlayer(event.getPlayer().getUniqueId()), new PacketOutProxyInfoUpdate(proxyInfo), new PacketOutSendControllerConsoleMessage("Player [Name=" + event.getPlayer().getName() + "/UUID=" + event.getPlayer().getUniqueId() + "/IP=" + event.getPlayer().getAddress().getAddress().getHostAddress() + "] is now disconnected"));
    }

    @EventHandler(priority = - 127)
    public void handle(final ServerKickEvent event) {
        if (event.getCancelServer() != null) {
            final ServerInfo serverInfo = ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getServerProcessManager().nextFreeLobby(event.getPlayer().getPermissions(), event.getPlayer().getServer().getInfo().getName());
            if (serverInfo != null) {
                event.setCancelled(true);
                event.setCancelServer(ProxyServer.getInstance().getServerInfo(serverInfo.getCloudProcess().getName()));
                event.setKickReasonComponent(event.getKickReasonComponent());
            } else {
                event.setCancelled(false);
                event.setCancelServer(null);
                event.setKickReasonComponent(TextComponent.fromLegacyText(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-connect-hub-no-server")));
            }
        }
    }
}
