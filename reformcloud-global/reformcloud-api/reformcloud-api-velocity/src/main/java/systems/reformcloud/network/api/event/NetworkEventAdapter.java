/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.api.event;

import io.netty.channel.ChannelHandlerContext;
import net.kyori.text.TextComponent;
import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.api.EventHandler;
import systems.reformcloud.bootstrap.VelocityBootstrap;
import systems.reformcloud.configurations.Configuration;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 05.03.2019
 */

public final class NetworkEventAdapter implements Serializable, EventHandler {

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
        VelocityBootstrap.getInstance().getProxyServer().getAllPlayers().forEach(proxiedPlayer ->
            proxiedPlayer.sendMessage(TextComponent.of(
                (ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getPrefix().endsWith(" ") ?
                    ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getPrefix() :
                    ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getPrefix() + " ") + "§cYour current proxy lost the connection to the cloud"
            )));
    }

    @Override
    public void channelExceptionCaught(ChannelHandlerContext channelHandlerContext,
        Throwable cause) {
    }
}
