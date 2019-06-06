/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api;

import systems.reformcloud.ReformCloudAPISpigot;
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
        ProxyInfo proxyInfo = this.findPlayer(uniqueID);
        if (proxyInfo == null) {
            return;
        }

        ReformCloudAPISpigot.getInstance().getChannelHandler().sendPacketSynchronized(
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
        ProxyInfo proxyInfo = this.findPlayer(uniqueID);
        if (proxyInfo == null) {
            return;
        }

        ReformCloudAPISpigot.getInstance().getChannelHandler().sendPacketSynchronized(
            "ReformCloudController", new PacketOutSendMessage(
                uniqueID, message, proxyInfo.getCloudProcess().getName()
            )
        );
    }

    @Override
    public void kickPlayer(UUID uniqueID, String reason) {
        ProxyInfo proxyInfo = this.findPlayer(uniqueID);
        if (proxyInfo == null) {
            return;
        }

        ReformCloudAPISpigot.getInstance().getChannelHandler().sendPacketSynchronized(
            "ReformCloudController", new PacketOutKickPlayer(
                uniqueID, reason, proxyInfo.getCloudProcess().getName()
            )
        );
    }

    private ProxyInfo findPlayer(UUID toFind) {
        return ReformCloudAPISpigot.getInstance()
            .getInternalCloudNetwork()
            .getServerProcessManager()
            .getAllRegisteredProxyProcesses()
            .stream()
            .filter(e -> e.getOnlinePlayers().contains(toFind))
            .findFirst()
            .orElse(null);
    }
}
