/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api;

import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.bootstrap.VelocityBootstrap;
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

public final class PlayerProvider implements Serializable, IDefaultPlayerProvider {
    @Override
    public void sendPlayer(UUID uniqueID, String name) {
        if (VelocityBootstrap.getInstance().getProxyServer().getPlayer(uniqueID).orElse(null) != null) {
            Player proxiedPlayer = VelocityBootstrap.getInstance().getProxyServer().getPlayer(uniqueID).get();
            proxiedPlayer.createConnectionRequest(VelocityBootstrap.getInstance().getProxyServer().getServer(name).get()).connect();
            return;
        }

        ProxyInfo proxyInfo = this.findPlayer(uniqueID);
        if (proxyInfo == null)
            return;

        ReformCloudAPIVelocity.getInstance().getChannelHandler().sendPacketSynchronized(
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
        if (VelocityBootstrap.getInstance().getProxyServer().getPlayer(uniqueID).orElse(null) != null) {
            Player proxiedPlayer = VelocityBootstrap.getInstance().getProxyServer().getPlayer(uniqueID).get();
            proxiedPlayer.sendMessage(TextComponent.of(message));
            return;
        }

        ProxyInfo proxyInfo = this.findPlayer(uniqueID);
        if (proxyInfo == null)
            return;

        ReformCloudAPIVelocity.getInstance().getChannelHandler().sendPacketSynchronized(
                "ReformCloudController", new PacketOutSendMessage(
                        uniqueID, message, proxyInfo.getCloudProcess().getName()
                )
        );
    }

    @Override
    public void kickPlayer(UUID uniqueID, String reason) {
        if (VelocityBootstrap.getInstance().getProxyServer().getPlayer(uniqueID).orElse(null) != null) {
            Player proxiedPlayer = VelocityBootstrap.getInstance().getProxyServer().getPlayer(uniqueID).get();
            proxiedPlayer.disconnect(reason == null ? TextComponent.of("§cNo reason defined")
                    : TextComponent.of(reason));
            return;
        }

        ProxyInfo proxyInfo = this.findPlayer(uniqueID);
        if (proxyInfo == null)
            return;

        ReformCloudAPIVelocity.getInstance().getChannelHandler().sendPacketSynchronized(
                "ReformCloudController", new PacketOutKickPlayer(
                        uniqueID, reason, proxyInfo.getCloudProcess().getName()
                )
        );
    }

    /**
     * @param toFind The uuid of the player which should be found
     * @return The player which is connected on a proxy server
     * or {@code null}
     */
    private ProxyInfo findPlayer(UUID toFind) {
        return ReformCloudAPIVelocity.getInstance()
                .getInternalCloudNetwork()
                .getServerProcessManager()
                .getAllRegisteredProxyProcesses()
                .stream()
                .filter(e -> e.getOnlinePlayers().contains(toFind))
                .findFirst()
                .orElse(null);
    }
}
