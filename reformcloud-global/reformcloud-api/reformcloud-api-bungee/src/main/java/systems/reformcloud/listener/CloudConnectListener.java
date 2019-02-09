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
import systems.reformcloud.netty.packets.PacketOutLoginPlayer;
import systems.reformcloud.netty.packets.PacketOutLogoutPlayer;
import systems.reformcloud.netty.packets.PacketOutProxyInfoUpdate;
import systems.reformcloud.netty.packets.PacketOutSendControllerConsoleMessage;

/**
 * @author _Klaro | Pasqual K. / created on 03.11.2018
 */

public final class CloudConnectListener implements Listener {
    @EventHandler(priority = - 127)
    public void handle(final ServerConnectEvent event) {
        if (event.getPlayer().getServer() == null) {
            final ServerInfo serverInfo = ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getServerProcessManager().nextFreeLobby(event.getPlayer().getPermissions());
            if (serverInfo != null)
                event.setTarget(ProxyServer.getInstance().getServerInfo(serverInfo.getCloudProcess().getName()));
            else event.setCancelled(true);
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

        if (proxyInfo.getProxyGroup().getMaxPlayers() <= BungeecordBootstrap.getInstance().getProxy().getPlayers().size())
            proxyInfo.setFull(true);
        else
            proxyInfo.setFull(false);

        proxyInfo.setOnline(BungeecordBootstrap.getInstance().getProxy().getPlayers().size());
        ReformCloudAPIBungee.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController", new PacketOutLoginPlayer(event.getConnection().getUniqueId()), new PacketOutProxyInfoUpdate(proxyInfo), new PacketOutSendControllerConsoleMessage("Player [Name=" + event.getConnection().getName() + "/UUID=" + event.getConnection().getUniqueId() + "/IP=" + event.getConnection().getAddress().getAddress().getHostAddress() + "] is now connected"));
    }

    @EventHandler(priority = - 128)
    public void handle(final PlayerDisconnectEvent event) {
        ProxyInfo proxyInfo = ReformCloudAPIBungee.getInstance().getProxyInfo();

        proxyInfo.getOnlinePlayers().remove(event.getPlayer().getUniqueId());

        if (proxyInfo.getProxyGroup().getMaxPlayers() <= BungeecordBootstrap.getInstance().getProxy().getPlayers().size())
            proxyInfo.setFull(true);
        else
            proxyInfo.setFull(false);

        proxyInfo.setOnline(BungeecordBootstrap.getInstance().getProxy().getPlayers().size());
        ReformCloudAPIBungee.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController", new PacketOutLogoutPlayer(event.getPlayer().getUniqueId()), new PacketOutProxyInfoUpdate(ReformCloudAPIBungee.getInstance().getProxyInfo()), new PacketOutSendControllerConsoleMessage("Player [Name=" + event.getPlayer().getName() + "/UUID=" + event.getPlayer().getUniqueId() + "/IP=" + event.getPlayer().getAddress().getAddress().getHostAddress() + "] is now disconnected"));
    }

    @EventHandler(priority = - 127)
    public void handle(final ServerKickEvent event) {
        final ServerInfo serverInfo = ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getServerProcessManager().nextFreeLobby(event.getPlayer().getPermissions());
        if (serverInfo != null) {
            event.setCancelServer(ProxyServer.getInstance().getServerInfo(serverInfo.getCloudProcess().getName()));
            event.setCancelled(true);
        } else {
            event.setCancelled(false);
            event.setCancelServer(null);
            event.getPlayer().sendMessage(TextComponent.fromLegacyText(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-connect-hub-no-server")));
        }
    }
}
