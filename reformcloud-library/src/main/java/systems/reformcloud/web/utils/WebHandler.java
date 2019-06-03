/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.web.utils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @author _Klaro | Pasqual K. / created on 24.11.2018
 */

public interface WebHandler {

    /**
     * The default web handler implementation
     *
     * @param channelHandlerContext The channel handler context of the channel which connects to the
     * controller
     * @param httpRequest The http request sent by the requester containing all needed information
     * @return The creates full http response to send back to the requester
     * @throws Exception If any exception occurs it will be handled here
     */
    FullHttpResponse handleRequest(ChannelHandlerContext channelHandlerContext,
        HttpRequest httpRequest) throws Exception;
}
