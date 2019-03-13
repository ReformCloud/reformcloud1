/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api;

import io.netty.channel.ChannelHandlerContext;
import systems.reformcloud.configurations.Configuration;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author _Klaro | Pasqual K. / created on 23.02.2019
 */

public interface IEventHandler {
    AtomicReference<IEventHandler> instance = new AtomicReference<>();

    void handleCustomPacket(String channel, String targetType, Configuration configuration);

    void handleReload();

    void channelConnected(ChannelHandlerContext channelHandlerContext);

    void channelDisconnected(ChannelHandlerContext channelHandlerContext);

    void channelExceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause);
}
