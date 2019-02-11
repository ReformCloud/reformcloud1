/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.launcher.SpigotBootstrap;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.packets.PacketOutCheckPlayer;
import systems.reformcloud.network.packets.PacketOutServerInfoUpdate;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public class PlayerConnectListener implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void handle(final AsyncPlayerPreLoginEvent event) {
        ReformCloudAPISpigot.getInstance().getChannelHandler().sendPacketSynchronized("ReformCloudController", new PacketOutCheckPlayer(event.getUniqueId()));
        ReformCloudLibraryService.sleep(25);
        if (!SpigotBootstrap.getInstance().getAcceptedPlayers().contains(event.getUniqueId())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-connect-only-proxy"));
            return;
        }

        if (ReformCloudAPISpigot.getInstance().getServerInfo().getServerGroup().isMaintenance()
                && SpigotBootstrap.getInstance().getServer().getPlayer(event.getUniqueId()) != null
                && !SpigotBootstrap.getInstance().getServer().getPlayer(event.getUniqueId()).hasPermission("reformcloud.join.server.maintenance")) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getMessage("internal-api-spigot-connect-no-permission"));
            return;
        }

        event.allow();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handle(final PlayerJoinEvent event) {
        final ServerInfo serverInfo = ReformCloudAPISpigot.getInstance().getServerInfo();

        if (serverInfo.getOnlinePlayers().contains(event.getPlayer().getUniqueId())) {
            event.getPlayer().kickPlayer("§cYou're already connected to this server");
            return;
        }

        serverInfo.getOnlinePlayers().add(event.getPlayer().getUniqueId());
        serverInfo.setOnline(serverInfo.getOnline() + 1);

        if (serverInfo.getOnline() <= serverInfo.getServerGroup().getMaxPlayers())
            serverInfo.setFull(true);
        else
            serverInfo.setFull(false);

        ReformCloudAPISpigot.getInstance().getChannelHandler().sendPacketSynchronized("ReformCloudController", new PacketOutServerInfoUpdate(serverInfo));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void handle(final PlayerQuitEvent event) {
        final ServerInfo serverInfo = ReformCloudAPISpigot.getInstance().getServerInfo();
        serverInfo.getOnlinePlayers().remove(event.getPlayer().getUniqueId());

        serverInfo.setOnline(serverInfo.getOnline() - 1);

        if (serverInfo.getOnline() <= serverInfo.getServerGroup().getMaxPlayers())
            serverInfo.setFull(true);
        else
            serverInfo.setFull(false);

        ReformCloudAPISpigot.getInstance().getChannelHandler().sendPacketSynchronized("ReformCloudController", new PacketOutServerInfoUpdate(serverInfo));
    }
}
