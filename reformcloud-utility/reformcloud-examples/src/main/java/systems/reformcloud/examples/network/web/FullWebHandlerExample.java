/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.examples.network.web;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import systems.reformcloud.web.utils.WebHandler;

import java.nio.charset.StandardCharsets;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

public class FullWebHandlerExample implements WebHandler {
    //For more Information about the Response, pleas visit the official Netty-Documentation
    @Override
    public FullHttpResponse handleRequest(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest) throws Exception {
        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(httpRequest.protocolVersion(), HttpResponseStatus.UNAUTHORIZED);
        StringBuilder answer = new StringBuilder();

        if (fullHttpResponse.headers().contains("heho"))
            answer.append("nope");
        else
            answer.append("heho");

        fullHttpResponse.setStatus(HttpResponseStatus.OK);
        fullHttpResponse.content().writeBytes(answer.toString().getBytes(StandardCharsets.UTF_8.name()));

        return fullHttpResponse;
    }
}
