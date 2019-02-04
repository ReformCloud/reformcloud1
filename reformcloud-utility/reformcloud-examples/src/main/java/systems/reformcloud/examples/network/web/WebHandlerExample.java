/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.examples.network.web;

import systems.reformcloud.web.utils.WebHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

public class WebHandlerExample implements WebHandler {
    /**
     * Get called when a webRequest triggers this handler
     */
    @Override
    public FullHttpResponse handleRequest(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest) throws Exception {
        return null;
    }
}
