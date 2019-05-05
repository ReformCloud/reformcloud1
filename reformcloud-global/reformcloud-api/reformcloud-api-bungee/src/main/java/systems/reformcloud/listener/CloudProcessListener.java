/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.listener;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.internal.events.CloudProxyAddEvent;
import systems.reformcloud.internal.events.CloudProxyRemoveEvent;
import systems.reformcloud.internal.events.CloudServerAddEvent;
import systems.reformcloud.internal.events.CloudServerRemoveEvent;
import systems.reformcloud.meta.enums.ServerModeType;

import java.net.InetSocketAddress;

/**
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

public final class CloudProcessListener implements Listener {

    @EventHandler(priority = -128)
    public void handle(final CloudServerAddEvent event) {
        ProxyServer.getInstance().getServers().put(
                event.getServerInfo().getCloudProcess().getName(),
                ProxyServer.getInstance().constructServerInfo(
                        event.getServerInfo().getCloudProcess().getName(),
                        new InetSocketAddress(event.getServerInfo().getHost(), event.getServerInfo().getPort()),
                        "ReformCloudServer",
                        false
                ));

        if (event.getServerInfo().getServerGroup().getServerModeType().equals(ServerModeType.LOBBY)) {
            ProxyServer.getInstance().getConfig().getListeners().forEach(listener ->
                    listener.getServerPriority().add(event.getServerInfo().getCloudProcess().getName())
            );
        }

        ProxyServer.getInstance().getPlayers().forEach(proxiedPlayer -> {
            if (proxiedPlayer.hasPermission("reformcloud.notify.server"))
                proxiedPlayer.sendMessage(TextComponent.fromLegacyText(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-startup-server").replaceAll("%server-name%", event.getServerInfo().getCloudProcess().getName())));
        });
    }

    @EventHandler(priority = -128)
    public void handle(final CloudProxyAddEvent event) {
        ProxyServer.getInstance().getPlayers().forEach(proxiedPlayer -> {
            if (proxiedPlayer.hasPermission("reformcloud.notify.proxy"))
                proxiedPlayer.sendMessage(TextComponent.fromLegacyText(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-startup-proxy").replaceAll("%proxy-name%", event.getProxyInfo().getCloudProcess().getName())));
        });
    }

    @EventHandler(priority = -128)
    public void handle(final CloudServerRemoveEvent event) {
        ProxyServer.getInstance().getServers().remove(event.getServerInfo().getCloudProcess().getName());

        if (event.getServerInfo().getServerGroup().getServerModeType().equals(ServerModeType.LOBBY)) {
            ProxyServer.getInstance().getConfig().getListeners().forEach(listener ->
                    listener.getServerPriority().remove(event.getServerInfo().getCloudProcess().getName())
            );
        }

        ProxyServer.getInstance().getPlayers().forEach(proxiedPlayer -> {
            if (proxiedPlayer.hasPermission("reformcloud.notify.server"))
                proxiedPlayer.sendMessage(TextComponent.fromLegacyText(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-remove-server").replaceAll("%server-name%", event.getServerInfo().getCloudProcess().getName())));
        });
    }

    @EventHandler(priority = -128)
    public void handle(final CloudProxyRemoveEvent event) {
        ProxyServer.getInstance().getPlayers().forEach(proxiedPlayer -> {
            if (proxiedPlayer.hasPermission("reformcloud.notify.proxy"))
                proxiedPlayer.sendMessage(TextComponent.fromLegacyText(ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-remove-proxy").replaceAll("%proxy-name%", event.getProxyInfo().getCloudProcess().getName())));
        });
    }
}
