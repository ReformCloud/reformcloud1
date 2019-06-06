/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.player.api;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.api.DefaultPlayerProvider;
import systems.reformcloud.launcher.BungeecordBootstrap;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.packets.PacketOutConnectPlayer;
import systems.reformcloud.network.packets.PacketOutKickPlayer;
import systems.reformcloud.network.packets.PacketOutSendMessage;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 14.03.2019
 */

public final class PlayerProvider implements Serializable, DefaultPlayerProvider {

    @Override
    public void sendPlayer(UUID uniqueID, String name) {
        if (BungeecordBootstrap.getInstance().getProxy().getPlayer(uniqueID) != null) {
            ProxiedPlayer proxiedPlayer = BungeecordBootstrap.getInstance().getProxy()
                .getPlayer(uniqueID);
            proxiedPlayer.connect(BungeecordBootstrap.getInstance().getProxy().getServerInfo(name));
            return;
        }

        ProxyInfo proxyInfo = this.findPlayer(uniqueID);
        if (proxyInfo == null) {
            return;
        }

        ReformCloudAPIBungee.getInstance().getChannelHandler().sendPacketSynchronized(
            "ReformCloudController", new PacketOutConnectPlayer(
                uniqueID, name, proxyInfo.getCloudProcess().getName()
            )
        );
    }

    @Override
    public void sendPlayer(UUID uniqueID, ServerInfo serverInfo) {
        this.sendPlayer(uniqueID, serverInfo.getCloudProcess().getName());
    }

    @Override
    public void sendMessage(UUID uniqueID, String message) {
        if (BungeecordBootstrap.getInstance().getProxy().getPlayer(uniqueID) != null) {
            ProxiedPlayer proxiedPlayer = BungeecordBootstrap.getInstance().getProxy()
                .getPlayer(uniqueID);
            proxiedPlayer.sendMessage(ChatMessageType.CHAT, TextComponent.fromLegacyText(message));
            return;
        }

        ProxyInfo proxyInfo = this.findPlayer(uniqueID);
        if (proxyInfo == null) {
            return;
        }

        ReformCloudAPIBungee.getInstance().getChannelHandler().sendPacketSynchronized(
            "ReformCloudController", new PacketOutSendMessage(
                uniqueID, message, proxyInfo.getCloudProcess().getName()
            )
        );
    }

    @Override
    public void kickPlayer(UUID uniqueID, String reason) {
        if (BungeecordBootstrap.getInstance().getProxy().getPlayer(uniqueID) != null) {
            ProxiedPlayer proxiedPlayer = BungeecordBootstrap.getInstance().getProxy()
                .getPlayer(uniqueID);
            proxiedPlayer
                .disconnect(reason == null ? TextComponent.fromLegacyText("§cNo reason defined")
                    : TextComponent.fromLegacyText(reason));
            return;
        }

        ProxyInfo proxyInfo = this.findPlayer(uniqueID);
        if (proxyInfo == null) {
            return;
        }

        ReformCloudAPIBungee.getInstance().getChannelHandler().sendPacketSynchronized(
            "ReformCloudController", new PacketOutKickPlayer(
                uniqueID, reason, proxyInfo.getCloudProcess().getName()
            )
        );
    }

    /**
     * @param toFind The uuid of the player which should be found
     * @return The player which is connected on a proxy server or {@code null}
     */
    private ProxyInfo findPlayer(UUID toFind) {
        return ReformCloudAPIBungee.getInstance()
            .getInternalCloudNetwork()
            .getServerProcessManager()
            .getAllRegisteredProxyProcesses()
            .stream()
            .filter(e -> e.getOnlinePlayers().contains(toFind))
            .findFirst()
            .orElse(null);
    }
}
