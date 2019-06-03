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

public interface EventHandler {

    /**
     * The atomic reference to get the default instance of the event handler
     */
    AtomicReference<EventHandler> instance = new AtomicReference<>();

    /**
     * This method will be called if a custom message was received
     *
     * @param channel The channel where the packet came from
     * @param targetType The type of the incoming packet
     * @param configuration The configuration of the packet
     */
    void handleCustomPacket(String channel, String targetType, Configuration configuration);

    /**
     * This method will be called when the network system reloads
     */
    void handleReload();

    /**
     * This method will be called when a channel connects
     *
     * @param channelHandlerContext The channelHandlerContext of the channel
     */
    void channelConnected(ChannelHandlerContext channelHandlerContext);

    /**
     * This method will be called when a channel disconnects
     *
     * @param channelHandlerContext The channelHandlerContext of the channel
     */
    void channelDisconnected(ChannelHandlerContext channelHandlerContext);

    /**
     * This method will be called when an exception caught in any channel
     *
     * @param channelHandlerContext The channelHandlerContext of the channel
     * @param cause The exception which was thrown
     */
    void channelExceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause);
}
