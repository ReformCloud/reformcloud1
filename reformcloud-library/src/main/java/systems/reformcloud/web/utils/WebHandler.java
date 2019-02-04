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
    FullHttpResponse handleRequest(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest) throws Exception;
}
