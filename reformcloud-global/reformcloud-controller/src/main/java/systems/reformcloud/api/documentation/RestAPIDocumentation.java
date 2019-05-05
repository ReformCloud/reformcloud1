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
import java.util.Collections;
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
        webHandlers.forEach(e -> stringBuilder.append("- Handler: " + e + "  "));
        fullHttpResponse.content().writeBytes(answer
                .addBooleanValue("success", true)
                .addValue("answer", Collections.singletonList(stringBuilder.substring(0)))
                .addStringValue("description: ", "To send an api request to the cloud system, you need the correct " +
                        "WebHandler path given below. Then you send an request to the api, for example with the postman " +
                        "application. If there are more arguments needed, or you have to authorize yourself, please " +
                        "check the result. The header will contain an error (then \"success\" is false) or a success " +
                        "message (then \"success\" is true)")
                .getJsonString().getBytes());
        fullHttpResponse.setStatus(HttpResponseStatus.OK);
        return fullHttpResponse;
    }
}
