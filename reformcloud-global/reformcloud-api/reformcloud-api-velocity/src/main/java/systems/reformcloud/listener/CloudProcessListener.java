/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.server.ServerInfo;
import net.kyori.text.TextComponent;
import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.bootstrap.VelocityBootstrap;
import systems.reformcloud.events.CloudProxyAddEvent;
import systems.reformcloud.events.CloudProxyRemoveEvent;
import systems.reformcloud.events.CloudServerAddEvent;
import systems.reformcloud.events.CloudServerRemoveEvent;
import systems.reformcloud.meta.enums.ServerModeType;

import java.net.InetSocketAddress;

/**
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

public final class CloudProcessListener {

    @Subscribe(order = PostOrder.FIRST)
    public void handle(final CloudServerAddEvent event) {
        VelocityBootstrap.getInstance().getProxyServer().registerServer(
                new ServerInfo(
                        event.getServerInfo().getCloudProcess().getName(),
                        new InetSocketAddress(event.getServerInfo().getHost(), event.getServerInfo().getPort())
                ));

        if (event.getServerInfo().getServerGroup().getServerModeType().equals(ServerModeType.LOBBY)) {
            VelocityBootstrap.getInstance().getProxyServer().getConfiguration().getAttemptConnectionOrder().add(
                    event.getServerInfo().getCloudProcess().getName()
            );
        }

        VelocityBootstrap.getInstance().getProxyServer().getAllPlayers().forEach(proxiedPlayer -> {
            if (proxiedPlayer.hasPermission("reformcloud.notify.server"))
                proxiedPlayer.sendMessage(TextComponent.of(ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-startup-server").replaceAll("%server-name%", event.getServerInfo().getCloudProcess().getName())));
        });
    }

    @Subscribe(order = PostOrder.FIRST)
    public void handle(final CloudProxyAddEvent event) {
        VelocityBootstrap.getInstance().getProxyServer().getAllPlayers().forEach(proxiedPlayer -> {
            if (proxiedPlayer.hasPermission("reformcloud.notify.proxy"))
                proxiedPlayer.sendMessage(TextComponent.of(ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-startup-proxy").replaceAll("%proxy-name%", event.getProxyInfo().getCloudProcess().getName())));
        });
    }

    @Subscribe(order = PostOrder.FIRST)
    public void handle(final CloudServerRemoveEvent event) {
        VelocityBootstrap.getInstance().getProxyServer().unregisterServer(
                VelocityBootstrap.getInstance().getProxyServer().getServer(event.getServerInfo().getCloudProcess().getName()).get().getServerInfo()
        );

        if (event.getServerInfo().getServerGroup().getServerModeType().equals(ServerModeType.LOBBY)) {
            VelocityBootstrap.getInstance().getProxyServer().getConfiguration().getAttemptConnectionOrder().remove(
                    event.getServerInfo().getCloudProcess().getName()
            );
        }

        VelocityBootstrap.getInstance().getProxyServer().getAllPlayers().forEach(proxiedPlayer -> {
            if (proxiedPlayer.hasPermission("reformcloud.notify.server"))
                proxiedPlayer.sendMessage(TextComponent.of(ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-remove-server").replaceAll("%server-name%", event.getServerInfo().getCloudProcess().getName())));
        });
    }

    @Subscribe(order = PostOrder.FIRST)
    public void handle(final CloudProxyRemoveEvent event) {
        VelocityBootstrap.getInstance().getProxyServer().getAllPlayers().forEach(proxiedPlayer -> {
            if (proxiedPlayer.hasPermission("reformcloud.notify.proxy"))
                proxiedPlayer.sendMessage(TextComponent.of(ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getMessage("internal-api-bungee-remove-proxy").replaceAll("%proxy-name%", event.getProxyInfo().getCloudProcess().getName())));
        });
    }
}
