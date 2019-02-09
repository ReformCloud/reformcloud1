/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api.documentation;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.api.utility.RestAPIUtility;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.web.utils.WebHandler;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;

/**
 * @author _Klaro | Pasqual K. / created on 09.02.2019
 */

public final class RestAPIDocumentation implements Serializable, WebHandler {
    @Override
    public FullHttpResponse handleRequest(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest) throws Exception {
        FullHttpResponse fullHttpResponse = RestAPIUtility.createFullHttpResponse(httpRequest.protocolVersion());
        Configuration answer = RestAPIUtility.createDefaultAnswer();

        Set<String> webHandlers = ReformCloudController
                .getInstance()
                .getReformWebServer()
                .getWebHandlerAdapter()
                .getWebHandlerMap()
                .keySet();
        StringBuilder stringBuilder = new StringBuilder();
        webHandlers.forEach(e -> stringBuilder.append("   -> Handler: " + e).append("\n"));
        fullHttpResponse.content().writeBytes(answer
                .addBooleanProperty("success", true)
                .addProperty("answer", Arrays.asList(stringBuilder.substring(0)))
                .getJsonString().getBytes());
        fullHttpResponse.setStatus(HttpResponseStatus.OK);
        return fullHttpResponse;
    }
}
