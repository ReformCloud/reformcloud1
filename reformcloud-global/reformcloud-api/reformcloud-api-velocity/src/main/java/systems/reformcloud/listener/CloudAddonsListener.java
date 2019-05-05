/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.permission.PermissionsSetupEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.PermissionProvider;
import com.velocitypowered.api.permission.PermissionSubject;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.optional.qual.MaybePresent;
import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.autoicon.IconManager;
import systems.reformcloud.bootstrap.VelocityBootstrap;
import systems.reformcloud.commands.ingame.command.IngameCommand;
import systems.reformcloud.events.CloudProxyInfoUpdateEvent;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.proxy.settings.ProxySettings;
import systems.reformcloud.network.packets.PacketOutCommandExecute;
import systems.reformcloud.network.packets.PacketOutUpdatePermissionHolder;
import systems.reformcloud.player.permissions.group.PermissionGroup;
import systems.reformcloud.player.permissions.player.PermissionHolder;
import systems.reformcloud.player.version.SpigotVersion;
import systems.reformcloud.utility.map.maps.Double;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 07.11.2018
 */

public final class CloudAddonsListener {
    @Subscribe
    public void handle(final PlayerChatEvent event) {
        final Player proxiedPlayer = event.getPlayer();

        if (event.getMessage().startsWith("/") && ReformCloudAPIVelocity.getInstance().getProxyInfo().getProxyGroup().isControllerCommandLogging())
            ReformCloudAPIVelocity.getInstance().getChannelHandler()
                    .sendPacketAsynchronous("ReformCloudController", new PacketOutCommandExecute(
                            proxiedPlayer.getUsername(),
                            proxiedPlayer.getUniqueId(),
                            event.getMessage(),
                            proxiedPlayer.getCurrentServer().get().getServerInfo().getName())
                    );

        IngameCommand ingameCommand = ReformCloudAPIVelocity.getInstance().getIngameCommand(event.getMessage());
        if (ingameCommand != null) {
            ReformCloudAPIVelocity.getInstance().executeIngameCommand(
                    ingameCommand,
                    event.getPlayer().getUniqueId(),
                    event.getMessage()
            );
        }
    }

    @Subscribe
    public void handle(final ProxyPingEvent event) {
        ServerPing serverPing = event.getPing();
        if (ReformCloudAPIVelocity.getInstance().getProxySettings() == null)
            return;

        if (IconManager.getInstance() != null && IconManager.getInstance().getCurrent() != null)
            serverPing = serverPing.asBuilder().favicon(IconManager.getInstance().getCurrent()).build();

        ProxySettings proxySettings = ReformCloudAPIVelocity.getInstance().getProxySettings();
        final ProxyGroup proxyGroup = ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork()
                .getProxyGroups().get(ReformCloudAPIVelocity.getInstance().getProxyInfo().getProxyGroup().getName());

        if (proxyGroup.isMaintenance()) {
            if (proxySettings.isMotdEnabled()) {
                Double<String, String> motd = proxySettings.getMaintenanceMotd().get(
                        ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextInt(proxySettings.getMaintenanceMotd().size())
                );
                serverPing = serverPing.asBuilder().description(
                        TextComponent.of(translateAlternateColorCodes('&', motd.getFirst() + "\n" + motd.getSecond())
                                .replace("%current_proxy%", ReformCloudAPIVelocity.getInstance().getProxyInfo().getCloudProcess().getName())
                                .replace("%current_group%", ReformCloudAPIVelocity.getInstance().getProxyInfo().getCloudProcess().getGroup())
                                .replace("%player_version%", SpigotVersion.getByProtocolId(event.getConnection().getProtocolVersion().getProtocol()).name())
                        )
                ).build();
            }

            ServerPing.SamplePlayer[] playerInfo = new ServerPing.SamplePlayer[proxySettings.getPlayerInfo().length];
            for (short i = 0; i < playerInfo.length; i++)
                playerInfo[i] = new ServerPing.SamplePlayer(translateAlternateColorCodes(
                        '&',
                        proxySettings.getPlayerInfo()[i]),
                        UUID.randomUUID()
                );

            serverPing = serverPing.asBuilder()
                    .samplePlayers(playerInfo)
                    .maximumPlayers(0)
                    .onlinePlayers(0).build();

            if (proxySettings.isProtocolEnabled()) {
                serverPing = serverPing.asBuilder().version(new ServerPing.Version(
                        1,
                        translateAlternateColorCodes('&', proxySettings.getMaintenanceProtocol()
                                .replace("%online_players%", Integer.toString(VelocityBootstrap.getInstance().getProxy().getPlayerCount()))
                                .replace("%max_players_global%", Integer.toString(ReformCloudAPIVelocity.getInstance().getGlobalMaxOnlineCount()))))
                ).build();
            }
        } else {
            if (proxySettings.isMotdEnabled()) {
                Double<String, String> motd = proxySettings.getNormalMotd().get(
                        ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextInt(proxySettings.getNormalMotd().size())
                );
                serverPing = serverPing.asBuilder().description(
                        TextComponent.of(
                                translateAlternateColorCodes('&', motd.getFirst() + "\n" + motd.getSecond())
                                        .replace("%current_proxy%", ReformCloudAPIVelocity.getInstance().getProxyInfo().getCloudProcess().getName())
                                        .replace("%current_group%", ReformCloudAPIVelocity.getInstance().getProxyInfo().getCloudProcess().getGroup())
                                        .replace("%player_version%", SpigotVersion.getByProtocolId(event.getConnection().getProtocolVersion().getProtocol()).name())
                        )
                ).build();
            }

            ServerPing.SamplePlayer[] playerInfo = new ServerPing.SamplePlayer[proxySettings.getPlayerInfo().length];
            for (short i = 0; i < playerInfo.length; i++)
                playerInfo[i] = new ServerPing.SamplePlayer(translateAlternateColorCodes(
                        '&',
                        proxySettings.getPlayerInfo()[i]),
                        UUID.randomUUID()
                );

            int max = proxySettings.isSlotCounter() ? ReformCloudAPIVelocity.getInstance().getGlobalMaxOnlineCount() + proxySettings.getMoreSlots() :
                    ReformCloudAPIVelocity.getInstance().getGlobalMaxOnlineCount();

            serverPing = serverPing.asBuilder()
                    .maximumPlayers(max)
                    .onlinePlayers(ReformCloudAPIVelocity.getInstance().getGlobalOnlineCount())
                    .samplePlayers(playerInfo).build();

            if (proxySettings.isProtocolEnabled()) {
                serverPing = serverPing.asBuilder().version(new ServerPing.Version(
                        1,
                        translateAlternateColorCodes('&', proxySettings.getProtocol()
                                .replace("%online_players%", Integer.toString(VelocityBootstrap.getInstance().getProxy().getPlayerCount()))
                                .replace("%max_players_global%", Integer.toString(ReformCloudAPIVelocity.getInstance().getGlobalMaxOnlineCount()))))
                ).build();
            }
        }

        event.setPing(serverPing);
    }

    @Subscribe
    public void handle(final PermissionsSetupEvent event) {
        if (ReformCloudAPIVelocity.getInstance().getPermissionCache() == null)
            return;

        System.out.println("-----");
        System.out.println(event.getSubject());
        System.out.println(event.getSubject() instanceof Player);
        System.out.println(event.toString());
        System.out.println("-----");
        if (event.getSubject() instanceof Player) {
            PermissionHolder permissionHolder = ReformCloudAPIVelocity.getInstance()
                    .getCachedPermissionHolders().get(((Player) event.getSubject()).getUniqueId());
            if (permissionHolder == null)
                return;

            Map<String, Long> copyOf = new HashMap<>(permissionHolder.getPermissionGroups());
            copyOf.forEach((groupName, timeout) -> {
                if (timeout != -1 && timeout <= System.currentTimeMillis())
                    permissionHolder.getPermissionGroups().remove(groupName);
            });

            if (copyOf.size() != permissionHolder.getPermissionGroups().size()) {
                if (permissionHolder.getPermissionGroups().size() == 0) {
                    permissionHolder.getPermissionGroups().put(
                            ReformCloudAPIVelocity.getInstance().getPermissionCache().getDefaultGroup().getName(), -1L
                    );
                }

                ReformCloudAPIVelocity.getInstance().getChannelHandler().sendPacketSynchronized(
                        "ReformCloudController", new PacketOutUpdatePermissionHolder(permissionHolder)
                );
            }

            event.setProvider(new PlayerPermissionProvider(((Player) event.getSubject()), permissionHolder));
        }
    }

    private class PlayerPermissionProvider implements PermissionProvider, PermissionFunction, PermissionSubject {
        private List<PermissionGroup> permissionGroups;
        private Player player;
        private PermissionHolder permissionHolder;

        private PlayerPermissionProvider(Player player, PermissionHolder permissionHolder) {
            System.out.println("INIT");
            System.out.println(player);
            System.out.println(permissionHolder);
            this.permissionGroups = permissionHolder.getAllPermissionGroups(ReformCloudAPIVelocity.getInstance().getPermissionCache());
            this.permissionHolder = permissionHolder;
            this.player = player;
        }

        @Override
        public @MaybePresent Tristate getPermissionValue(@MaybePresent String s) {
            return Tristate.fromNullableBoolean(permissionHolder.hasPermission(s, permissionGroups));
        }

        @Override
        public boolean hasPermission(String permission) {
            return getPermissionValue(permission).equals(Tristate.TRUE);
        }

        @Override
        public @MaybePresent PermissionFunction createFunction(@MaybePresent PermissionSubject permissionSubject) {
            if (this.player != permissionSubject)
                throw new IllegalStateException("CreateFunction called with different argument");

            return this;
        }
    }

    private int onlineCount = 0;

    @Subscribe(order = PostOrder.EARLY)
    public void handle(final CloudProxyInfoUpdateEvent event) {
        int current = ReformCloudAPIVelocity.getInstance().getGlobalOnlineCount();
        if (current != onlineCount) {
            onlineCount = current;
            VelocityBootstrap.getInstance().getProxy().getAllPlayers().forEach(e -> {
                if (e.getCurrentServer().isPresent())
                    CloudConnectListener.initTab(e);
            });
        }
    }

    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        char[] b = textToTranslate.toCharArray();

        for (int i = 0; i < b.length - 1; ++i) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = 167;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }

        return new String(b);
    }
}
