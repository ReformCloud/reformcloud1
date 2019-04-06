/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.listener;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.launcher.BungeecordBootstrap;
import systems.reformcloud.meta.proxy.ProxyGroup;

/**
 * @author _Klaro | Pasqual K. / created on 02.11.2018
 */

public final class CloudProxyPingListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handle(final ProxyPingEvent event) {
        if (ReformCloudAPIBungee.getInstance().getProxySettings() != null)
            return;

        final ProxyGroup proxyGroup = ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                .getProxyGroups().get(ReformCloudAPIBungee.getInstance().getProxyInfo().getProxyGroup().getName());
        if (proxyGroup == null)
            return;

        event.getResponse().setPlayers(new ServerPing.Players(
                proxyGroup.getMaxPlayers(),
                BungeecordBootstrap.getInstance().getProxy().getPlayers().size(),
                event.getResponse().getPlayers().getSample()
        ));

        event.setResponse(event.getResponse());
    }
}
