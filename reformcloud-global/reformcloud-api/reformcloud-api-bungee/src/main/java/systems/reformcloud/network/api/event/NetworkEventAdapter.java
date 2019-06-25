/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.api.event;

import io.netty.channel.ChannelHandlerContext;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.api.EventHandler;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.launcher.BungeecordBootstrap;

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
        BungeecordBootstrap.getInstance().getProxy().getPlayers().forEach(proxiedPlayer ->
            proxiedPlayer.sendMessage(ChatMessageType.CHAT, TextComponent.fromLegacyText(
                (ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getPrefix().endsWith(" ") ?
                    ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getPrefix() :
                    ReformCloudAPIBungee.getInstance().getInternalCloudNetwork().getPrefix() + " ") + "§cYour current proxy lost the connection to the cloud"
            )));
    }

    @Override
    public void channelExceptionCaught(ChannelHandlerContext channelHandlerContext,
        Throwable cause) {
    }
}
