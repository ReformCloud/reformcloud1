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
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.web.WebUser;
import systems.reformcloud.network.out.PacketOutStopProcess;
import systems.reformcloud.web.utils.WebHandler;

import java.io.Serializable;
import java.util.Collections;

/**
 * @author _Klaro | Pasqual K. / created on 09.02.2019
 */

public final class RestAPIStopServer implements Serializable, WebHandler {

    @Override
    public FullHttpResponse handleRequest(ChannelHandlerContext channelHandlerContext,
        HttpRequest httpRequest) throws Exception {
        FullHttpResponse fullHttpResponse = RestAPIUtility
            .createFullHttpResponse(httpRequest.protocolVersion());
        Configuration answer = RestAPIUtility.createDefaultAnswer();

        final HttpHeaders httpHeaders = httpRequest.headers();
        if (!httpHeaders.contains("-XUser")
            || !httpHeaders.contains("-XPassword")
            || !httpHeaders.contains("-XServer")) {
            answer.addValue("response",
                Collections.singletonList("No -XUser, -XPassword or -XServer provided"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            return fullHttpResponse;
        }

        WebUser webUser = ReformCloudController.getInstance()
            .getCloudConfiguration()
            .getWebUsers()
            .stream()
            .filter(e -> e.getUserName().equals(httpHeaders.get("-XUser")))
            .findFirst()
            .orElse(null);
        if (webUser == null) {
            answer
                .addValue("response", Collections.singletonList("User by given -XUser not found"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            return fullHttpResponse;
        }

        if (!webUser.getPassword()
            .equals(StringEncrypt.encryptSHA512(httpHeaders.get("-XPassword")))) {
            answer.addValue("response",
                Collections.singletonList("Password given by -XPassword incorrect"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            return fullHttpResponse;
        }

        if (!RestAPIUtility.hasPermission(webUser, "web.api.stop.server")) {
            answer.addValue("response", Collections.singletonList("Permission denied"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            return fullHttpResponse;
        }

        ServerInfo serverInfo = ReformCloudController
            .getInstance()
            .getInternalCloudNetwork()
            .getServerProcessManager()
            .getRegisteredServerByName(httpHeaders.get("-XServer"));
        if (serverInfo == null) {
            answer.addValue("answer",
                Collections.singletonList("Server not registered in CloudNetwork"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            return fullHttpResponse;
        }

        ReformCloudController.getInstance()
            .getChannelHandler()
            .sendPacketAsynchronous(
                serverInfo.getCloudProcess().getClient(),
                new PacketOutStopProcess(serverInfo.getCloudProcess().getName())
            );

        answer.addBooleanValue("success", true)
            .addValue("answer", Collections.singletonList("Trying to stop process"));
        fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
        fullHttpResponse.setStatus(HttpResponseStatus.OK);
        return fullHttpResponse;
    }
}
