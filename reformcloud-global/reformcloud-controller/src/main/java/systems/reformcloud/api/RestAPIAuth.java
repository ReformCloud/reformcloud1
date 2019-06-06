/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.api.utility.RestAPIUtility;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.cryptic.StringEncrypt;
import systems.reformcloud.meta.web.WebUser;
import systems.reformcloud.web.utils.WebHandler;

import java.io.Serializable;
import java.util.Collections;

/**
 * @author _Klaro | Pasqual K. / created on 08.02.2019
 */

public final class RestAPIAuth implements Serializable, WebHandler {

    @Override
    public FullHttpResponse handleRequest(ChannelHandlerContext channelHandlerContext,
        HttpRequest httpRequest) throws Exception {
        FullHttpResponse fullHttpResponse = RestAPIUtility
            .createFullHttpResponse(httpRequest.protocolVersion());
        Configuration answer = RestAPIUtility.createDefaultAnswer();

        final HttpHeaders httpHeaders = httpRequest.headers();
        if (!httpHeaders.contains("-XUser") || !httpHeaders.contains("-XPassword")) {
            answer.addValue("response",
                Collections.singletonList("No -XUser or -XPassword provided"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            fullHttpResponse.setStatus(HttpResponseStatus.OK);
            return fullHttpResponse;
        }

        WebUser webUser = ReformCloudController.getInstance()
            .getCloudConfiguration()
            .getWebUsers()
            .stream()
            .filter(e -> e.getUser().equals(httpHeaders.get("-XUser")))
            .findFirst()
            .orElse(null);
        if (webUser == null) {
            answer
                .addValue("response", Collections.singletonList("User by given -XUser not found"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            fullHttpResponse.setStatus(HttpResponseStatus.OK);
            return fullHttpResponse;
        }

        if (!webUser.getPassword()
            .equals(StringEncrypt.encryptSHA512(httpHeaders.get("-XPassword")))) {
            answer.addValue("response",
                Collections.singletonList("Password given by -XPassword incorrect"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            fullHttpResponse.setStatus(HttpResponseStatus.OK);
            return fullHttpResponse;
        }

        answer.addValue("success", true)
            .addValue("response", Collections.singletonList("Authentication successful"));
        fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
        fullHttpResponse.setStatus(HttpResponseStatus.OK);

        return fullHttpResponse;
    }
}
