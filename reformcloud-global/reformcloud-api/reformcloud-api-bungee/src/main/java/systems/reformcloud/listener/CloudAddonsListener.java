/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.listener;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.commands.ingame.command.IngameCommand;
import systems.reformcloud.launcher.BungeecordBootstrap;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.proxy.settings.ProxySettings;
import systems.reformcloud.network.packets.PacketOutCommandExecute;
import systems.reformcloud.network.packets.PacketOutUpdatePermissionHolder;
import systems.reformcloud.player.permissions.group.PermissionGroup;
import systems.reformcloud.player.permissions.player.PermissionHolder;
import systems.reformcloud.utility.map.maps.Double;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author _Klaro | Pasqual K. / created on 07.11.2018
 */

public final class CloudAddonsListener implements Listener {
    @EventHandler
    public void handle(final ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer)) return;
        final ProxiedPlayer proxiedPlayer = (ProxiedPlayer) event.getSender();

        if (event.isCommand() && !event.isCancelled() && ReformCloudAPIBungee.getInstance().getProxyInfo().getProxyGroup().isControllerCommandLogging())
            ReformCloudAPIBungee.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController", new PacketOutCommandExecute(proxiedPlayer.getName(), proxiedPlayer.getUniqueId(), event.getMessage(), proxiedPlayer.getServer().getInfo().getName()));

        IngameCommand ingameCommand = ReformCloudAPIBungee.getInstance().getIngameCommand(event.getMessage());
        if (ingameCommand != null) {
            ReformCloudAPIBungee.getInstance().executeIngameCommand(
                    ingameCommand,
                    ((ProxiedPlayer) event.getSender()).getUniqueId(),
                    event.getMessage()
            );
        }
    }

    @EventHandler
    public void handle(final PermissionCheckEvent event) {
        if (ReformCloudAPIBungee.getInstance().getPermissionCache() == null)
            return;

        if (event.getSender() instanceof ProxiedPlayer) {
            PermissionHolder permissionHolder = ReformCloudAPIBungee.getInstance()
                    .getCachedPermissionHolders().get(((ProxiedPlayer) event.getSender()).getUniqueId());
            if (permissionHolder == null) {
                event.setHasPermission(false);
                return;
            }

            Map<String, Long> copyOf = new HashMap<>(permissionHolder.getPermissionGroups());

            copyOf.forEach((groupName, timeout) -> {
                if (timeout != -1 && timeout <= System.currentTimeMillis())
                    permissionHolder.getPermissionGroups().remove(groupName);
            });

            if (copyOf.size() != permissionHolder.getPermissionGroups().size()) {
                if (permissionHolder.getPermissionGroups().size() == 0) {
                    permissionHolder.getPermissionGroups().put(
                            ReformCloudAPIBungee.getInstance().getPermissionCache().getDefaultGroup().getName(), -1L
                    );
                }

                ReformCloudAPIBungee.getInstance().getChannelHandler().sendPacketSynchronized(
                        "ReformCloudController", new PacketOutUpdatePermissionHolder(permissionHolder)
                );
            }

            List<PermissionGroup> permissionGroups = ReformCloudAPIBungee.getInstance()
                    .getPermissionCache().getAllRegisteredGroups().stream().filter(e -> permissionHolder
                            .getPermissionGroups().containsKey(e.getName()))
                    .collect(Collectors.toList());
            if (permissionHolder.getPermissionGroups().containsKey(ReformCloudAPIBungee.getInstance()
                    .getPermissionCache().getDefaultGroup().getName())) {
                permissionGroups.add(ReformCloudAPIBungee.getInstance().getPermissionCache().getDefaultGroup());
            }

            event.setHasPermission(permissionHolder.hasPermission(event.getPermission(), permissionGroups));
        } else
            event.setHasPermission(true);
    }

    @EventHandler
    public void handle(final ProxyPingEvent event) {
        if (ReformCloudAPIBungee.getInstance().getProxySettings() == null)
            return;

        ServerPing serverPing = event.getResponse();
        ProxySettings proxySettings = ReformCloudAPIBungee.getInstance().getProxySettings();
        final ProxyGroup proxyGroup = ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
                .getProxyGroups().get(ReformCloudAPIBungee.getInstance().getProxyInfo().getProxyGroup().getName());

        if (proxyGroup.isMaintenance()) {
            if (proxySettings.isMotdEnabled()) {
                Double<String, String> motd = proxySettings.getMaintenanceMotd().get(
                        ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextInt(proxySettings.getMaintenanceMotd().size())
                );
                serverPing.setDescriptionComponent(
                        new TextComponent(TextComponent.fromLegacyText(
                                ChatColor.translateAlternateColorCodes('&', motd.getFirst() + "\n" + motd.getSecond()))
                        ));
            }

            ServerPing.PlayerInfo[] playerInfo = new ServerPing.PlayerInfo[proxySettings.getPlayerInfo().length];
            for (short i = 0; i < playerInfo.length; i++)
                playerInfo[i] = new ServerPing.PlayerInfo(ChatColor.translateAlternateColorCodes(
                        '&',
                        proxySettings.getPlayerInfo()[i]),
                        UUID.randomUUID()
                );

            serverPing.setPlayers(new ServerPing.Players(0, 0, playerInfo));

            if (proxySettings.isProtocolEnabled()) {
                serverPing.setVersion(new ServerPing.Protocol(
                        ChatColor.translateAlternateColorCodes('&', proxySettings.getMaintenanceProtocol()
                                .replace("%online_players%", Integer.toString(BungeecordBootstrap.getInstance().getProxy().getOnlineCount()))
                                .replace("%max_players_global%", Integer.toString(ReformCloudAPIBungee.getInstance().getGlobalMaxOnlineCount()))),
                        1)
                );
            }
        } else {
            if (proxySettings.isMotdEnabled()) {
                Double<String, String> motd = proxySettings.getNormalMotd().get(
                        ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextInt(proxySettings.getNormalMotd().size())
                );
                serverPing.setDescriptionComponent(
                        new TextComponent(TextComponent.fromLegacyText(
                                ChatColor.translateAlternateColorCodes('&', motd.getFirst() + "\n" + motd.getSecond()))
                        ));
            }

            ServerPing.PlayerInfo[] playerInfo = new ServerPing.PlayerInfo[proxySettings.getPlayerInfo().length];
            for (short i = 0; i < playerInfo.length; i++)
                playerInfo[i] = new ServerPing.PlayerInfo(ChatColor.translateAlternateColorCodes(
                        '&',
                        proxySettings.getPlayerInfo()[i]),
                        UUID.randomUUID()
                );

            int max = proxySettings.isSlotCounter() ? ReformCloudAPIBungee.getInstance().getGlobalMaxOnlineCount() + proxySettings.getMoreSlots() :
                    ReformCloudAPIBungee.getInstance().getGlobalMaxOnlineCount();

            serverPing.setPlayers(new ServerPing.Players(max, ReformCloudAPIBungee.getInstance().getGlobalOnlineCount(), playerInfo));

            if (proxySettings.isProtocolEnabled()) {
                serverPing.setVersion(new ServerPing.Protocol(
                        ChatColor.translateAlternateColorCodes('&', proxySettings.getProtocol()
                                .replace("%online_players%", Integer.toString(BungeecordBootstrap.getInstance().getProxy().getOnlineCount())
                                        .replace("%max_players_global%", Integer.toString(ReformCloudAPIBungee.getInstance().getGlobalMaxOnlineCount())))),
                        1)
                );
            }
        }
    }
}
