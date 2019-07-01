/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.updater;

import org.bukkit.Bukkit;
import org.bukkit.event.server.ServerListPingEvent;
import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.launcher.SpigotBootstrap;
import systems.reformcloud.meta.enums.ServerState;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * @author _Klaro | Pasqual K. / created on 25.06.2019
 */

public final class ServerListUpdater extends Thread implements Serializable {

    private final InetAddress inetAddress = new InetSocketAddress(
        "127.0.0.1", 50000
    ).getAddress();

    public ServerListUpdater() {
        SpigotBootstrap.getInstance().getServer().getScheduler().runTaskTimer(
            SpigotBootstrap.getInstance(), () -> {
                try {
                    String motd =
                        ReformCloudAPISpigot.getInstance().getServerInfo().getMotd();
                    int max =
                        ReformCloudAPISpigot.getInstance().getServerInfo().getServerGroup().getMaxPlayers();

                    boolean hasInfoChanged = false;
                    boolean hasGroupChanged = false;

                    ServerListPingEvent serverListPingEvent = new ServerListPingEvent(
                        inetAddress,
                        motd,
                        Bukkit.getOnlinePlayers().size(),
                        max
                    );
                    Bukkit.getServer().getPluginManager().callEvent(serverListPingEvent);
                    if (!serverListPingEvent.getMotd().equals(motd)) {
                        ReformCloudAPISpigot.getInstance().getServerInfo().setMotd(motd);

                        if (serverListPingEvent.getMotd().toLowerCase().contains("reformcloud_hidden")
                            && !ReformCloudAPISpigot.getInstance().getServerInfo().getServerState().equals(ServerState.HIDDEN)) {
                            ReformCloudAPISpigot.getInstance().getServerInfo().setServerState(ServerState.HIDDEN);
                        }

                        hasInfoChanged = true;
                    }

                    if (serverListPingEvent.getMaxPlayers() != max) {
                        ReformCloudAPISpigot.getInstance().getServerInfo().getServerGroup().setMaxPlayers(
                            serverListPingEvent.getMaxPlayers()
                        );
                        hasGroupChanged = true;
                    }

                    if (hasInfoChanged) {
                        ReformCloudAPISpigot.getInstance().update();
                    }

                    if (hasGroupChanged) {
                        ReformCloudAPISpigot.getInstance().updateServerGroup(
                            ReformCloudAPISpigot.getInstance().getServerInfo().getServerGroup()
                        );
                    }
                } catch (final Throwable ignored) {
                }
            }, 0, 5
        );
    }
}
