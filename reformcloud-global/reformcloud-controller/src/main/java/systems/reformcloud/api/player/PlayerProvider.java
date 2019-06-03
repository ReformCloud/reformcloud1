/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api.player;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.api.DefaultPlayerProvider;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.out.PacketOutConnectPlayer;
import systems.reformcloud.network.out.PacketOutKickPlayer;
import systems.reformcloud.network.out.PacketOutSendPlayerMessage;

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

        ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
            proxyInfo.getCloudProcess().getName(), new PacketOutConnectPlayer(
                uniqueID, name
            )
        );
    }

    @Override
    public void sendPlayer(UUID uniqueID, ServerInfo serverInfo) {
        this.sendMessage(uniqueID, serverInfo.getCloudProcess().getName());
    }

    @Override
    public void sendMessage(UUID uniqueID, String message) {
        ProxyInfo proxyInfo = this.findPlayer(uniqueID);
        if (proxyInfo == null) {
            return;
        }

        ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
            proxyInfo.getCloudProcess().getName(), new PacketOutSendPlayerMessage(
                uniqueID, message
            )
        );
    }

    @Override
    public void kickPlayer(UUID uniqueID, String reason) {
        ProxyInfo proxyInfo = this.findPlayer(uniqueID);
        if (proxyInfo == null) {
            return;
        }

        ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
            proxyInfo.getCloudProcess().getName(), new PacketOutKickPlayer(
                uniqueID, reason
            )
        );
    }

    private ProxyInfo findPlayer(UUID toFind) {
        return ReformCloudController.getInstance()
            .getInternalCloudNetwork()
            .getServerProcessManager()
            .getAllRegisteredProxyProcesses()
            .stream()
            .filter(e -> e.getOnlinePlayers().contains(toFind))
            .findFirst()
            .orElse(null);
    }
}
