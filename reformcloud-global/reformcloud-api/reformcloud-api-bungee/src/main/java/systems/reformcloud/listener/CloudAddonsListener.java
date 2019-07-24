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
import systems.reformcloud.autoicon.IconManager;
import systems.reformcloud.commands.ingame.command.IngameCommand;
import systems.reformcloud.internal.events.CloudProxyInfoUpdateEvent;
import systems.reformcloud.launcher.BungeecordBootstrap;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.proxy.settings.ProxySettings;
import systems.reformcloud.network.packets.PacketOutCommandExecute;
import systems.reformcloud.permissions.PermissionUtil;
import systems.reformcloud.player.version.SpigotVersion;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.map.maps.Double;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 07.11.2018
 */

public final class CloudAddonsListener implements Listener {

    @EventHandler
    public void handle(final ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer)) {
            return;
        }
        final ProxiedPlayer proxiedPlayer = (ProxiedPlayer) event.getSender();

        if (event.isCommand() && !event.isCancelled() && ReformCloudAPIBungee.getInstance()
            .getProxyInfo().getProxyGroup().isControllerCommandLogging()) {
            ReformCloudAPIBungee.getInstance().getChannelHandler()
                .sendPacketAsynchronous("ReformCloudController",
                    new PacketOutCommandExecute(proxiedPlayer.getName(),
                        proxiedPlayer.getUniqueId(), event.getMessage(),
                        proxiedPlayer.getServer().getInfo().getName()));
        }

        IngameCommand ingameCommand = ReformCloudAPIBungee.getInstance()
            .getIngameCommand(event.getMessage());
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
        if (ReformCloudAPIBungee.getInstance().getPermissionCache() == null) {
            return;
        }

        event.setHasPermission(
            PermissionUtil.hasPermission(event.getSender(), event.getPermission()));
    }

    @EventHandler
    public void handle(final ProxyPingEvent event) {
        ServerPing serverPing = event.getResponse();
        if (ReformCloudAPIBungee.getInstance().getProxySettings() == null) {
            return;
        }

        if (IconManager.getInstance() != null && IconManager.getInstance().getCurrent() != null) {
            serverPing.setFavicon(IconManager.getInstance().getCurrent());
        }

        ProxySettings proxySettings = ReformCloudAPIBungee.getInstance().getProxySettings();
        final ProxyGroup proxyGroup = ReformCloudAPIBungee.getInstance().getInternalCloudNetwork()
            .getProxyGroups()
            .get(ReformCloudAPIBungee.getInstance().getProxyInfo().getProxyGroup().getName());

        if (proxyGroup.isMaintenance()) {
            if (proxySettings.isMotdEnabled()) {
                Double<String, String> motd = proxySettings.getMaintenanceMotd().get(
                    ReformCloudLibraryService.THREAD_LOCAL_RANDOM
                        .nextInt(proxySettings.getMaintenanceMotd().size())
                );
                serverPing.setDescriptionComponent(
                    new TextComponent(TextComponent.fromLegacyText(
                        ChatColor.translateAlternateColorCodes('&',
                            motd.getFirst() + "\n" + motd.getSecond())
                            .replace("%current_proxy%",
                                ReformCloudAPIBungee.getInstance().getProxyInfo().getCloudProcess()
                                    .getName())
                            .replace("%current_group%",
                                ReformCloudAPIBungee.getInstance().getProxyInfo().getCloudProcess()
                                    .getGroup())
                            .replace("%player_name%",
                                event.getConnection().getName() != null ? event.getConnection()
                                    .getName() : StringUtil.NULL)
                            .replace("%player_version%",
                                SpigotVersion.getByProtocolId(event.getConnection().getVersion())
                                    .name())
                    )));
            }

            ServerPing.PlayerInfo[] playerInfo = new ServerPing.PlayerInfo[proxySettings
                .getPlayerInfo().length];
            for (short i = 0; i < playerInfo.length; i++) {
                playerInfo[i] = new ServerPing.PlayerInfo(ChatColor.translateAlternateColorCodes(
                    '&',
                    proxySettings.getPlayerInfo()[i]),
                    UUID.randomUUID()
                );
            }

            serverPing.setPlayers(new ServerPing.Players(0, 0, playerInfo));

            if (proxySettings.isProtocolEnabled()) {
                serverPing.setVersion(new ServerPing.Protocol(
                    ChatColor
                        .translateAlternateColorCodes('&', proxySettings.getMaintenanceProtocol()
                            .replace("%online_players%", Integer.toString(
                                BungeecordBootstrap.getInstance().getProxy().getOnlineCount()))
                            .replace("%max_players_global%", Integer.toString(
                                ReformCloudAPIBungee.getInstance().getGlobalMaxOnlineCount()))),
                    1)
                );
            }
        } else {
            if (proxySettings.isMotdEnabled()) {
                Double<String, String> motd = proxySettings.getNormalMotd().get(
                    ReformCloudLibraryService.THREAD_LOCAL_RANDOM
                        .nextInt(proxySettings.getNormalMotd().size())
                );
                serverPing.setDescriptionComponent(
                    new TextComponent(TextComponent.fromLegacyText(
                        ChatColor.translateAlternateColorCodes('&',
                            motd.getFirst() + "\n" + motd.getSecond())
                            .replace("%current_proxy%",
                                ReformCloudAPIBungee.getInstance().getProxyInfo().getCloudProcess()
                                    .getName())
                            .replace("%current_group%",
                                ReformCloudAPIBungee.getInstance().getProxyInfo().getCloudProcess()
                                    .getGroup())
                            .replace("%player_name%",
                                event.getConnection().getName() != null ? event.getConnection()
                                    .getName() : StringUtil.NULL)
                            .replace("%player_version%",
                                SpigotVersion.getByProtocolId(event.getConnection().getVersion())
                                    .name())
                    )));
            }

            ServerPing.PlayerInfo[] playerInfo = new ServerPing.PlayerInfo[proxySettings
                .getPlayerInfo().length];
            for (short i = 0; i < playerInfo.length; i++) {
                playerInfo[i] = new ServerPing.PlayerInfo(ChatColor.translateAlternateColorCodes(
                    '&',
                    proxySettings.getPlayerInfo()[i]),
                    UUID.randomUUID()
                );
            }

            int max = proxySettings.isSlotCounter() ?
                ReformCloudAPIBungee.getInstance().getGlobalMaxOnlineCount() + proxySettings
                    .getMoreSlots() :
                ReformCloudAPIBungee.getInstance().getGlobalMaxOnlineCount();

            serverPing.setPlayers(new ServerPing.Players(max,
                ReformCloudAPIBungee.getInstance().getGlobalOnlineCount(), playerInfo));

            if (proxySettings.isProtocolEnabled()) {
                serverPing.setVersion(new ServerPing.Protocol(
                    ChatColor.translateAlternateColorCodes('&', proxySettings.getProtocol()
                        .replace("%online_players%", Integer.toString(
                            BungeecordBootstrap.getInstance().getProxy().getOnlineCount()))
                        .replace("%max_players_global%", Integer.toString(
                            ReformCloudAPIBungee.getInstance().getGlobalMaxOnlineCount()))),
                    1)
                );
            }
        }

        event.setResponse(event.getResponse());
    }

    private int online = 0;

    @EventHandler(priority = -125)
    public void handle(final CloudProxyInfoUpdateEvent event) {
        int current = ReformCloudAPIBungee.getInstance().getGlobalOnlineCount();
        if (current != online) {
            online = current;
            BungeecordBootstrap.getInstance().getProxy().getPlayers().forEach(e -> {
                if (e.getServer() == null || e.getServer().getInfo() == null
                    || e.getServer().getInfo().getName() == null) {
                    return;
                }

                CloudConnectListener.initTab(e);
            });
        }
    }
}
