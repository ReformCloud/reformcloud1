/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.api.event;

import io.netty.channel.ChannelHandlerContext;
import systems.reformcloud.api.EventHandler;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.launcher.SpigotBootstrap;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 05.03.2019
 */

public final class NetworkEventHandler implements Serializable, EventHandler {

    @Override
    public void handleCustomPacket(String channel, String targetType, Configuration configuration) {
    }

    @Override
    public void handleReload() {
    }

    @Override
    public void channelConnected(ChannelHandlerContext channelHandlerContext) {

    }

    @Override
    public void channelDisconnected(ChannelHandlerContext channelHandlerContext) {
        SpigotBootstrap.getInstance().getServer().getOnlinePlayers().forEach(player ->
            player.sendMessage("Your current server lost the connection to the cloud")
        );
    }

    @Override
    public void channelExceptionCaught(ChannelHandlerContext channelHandlerContext,
        Throwable cause) {

    }
}
