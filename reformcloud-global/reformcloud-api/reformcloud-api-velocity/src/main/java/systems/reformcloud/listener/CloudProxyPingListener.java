/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.autoicon.IconManager;
import systems.reformcloud.bootstrap.VelocityBootstrap;
import systems.reformcloud.meta.proxy.ProxyGroup;

/**
 * @author _Klaro | Pasqual K. / created on 02.11.2018
 */

public final class CloudProxyPingListener {
    @Subscribe(order = PostOrder.LATE)
    public void handle(final ProxyPingEvent event) {
        if (ReformCloudAPIVelocity.getInstance().getProxySettings() != null)
            return;

        ServerPing serverPing = event.getPing();

        if (IconManager.getInstance() != null && IconManager.getInstance().getCurrent() != null)
            serverPing = serverPing.asBuilder().favicon(IconManager.getInstance().getCurrent()).build();

        final ProxyGroup proxyGroup = ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getProxyGroups().get(ReformCloudAPIVelocity.getInstance().getProxyInfo().getProxyGroup().getName());
        if (proxyGroup == null)
            return;

        event.setPing(serverPing.asBuilder()
                .maximumPlayers(proxyGroup.getMaxPlayers())
                .onlinePlayers(VelocityBootstrap.getInstance().getProxyServer().getPlayerCount())
                .build()
        );
    }
}
