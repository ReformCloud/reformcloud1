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
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.web.WebUser;
import systems.reformcloud.netty.out.PacketOutStopProcess;
import systems.reformcloud.web.utils.WebHandler;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author _Klaro | Pasqual K. / created on 09.02.2019
 */

public final class RestAPIStopServer implements Serializable, WebHandler {
    @Override
    public FullHttpResponse handleRequest(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest) throws Exception {
        FullHttpResponse fullHttpResponse = RestAPIUtility.createFullHttpResponse(httpRequest.protocolVersion());
        Configuration answer = RestAPIUtility.createDefaultAnswer();

        final HttpHeaders httpHeaders = httpRequest.headers();
        if (!httpHeaders.contains("-XUser")
                || !httpHeaders.contains("-XPassword")
                || !httpHeaders.contains("-XServer")) {
            answer.addProperty("response", Arrays.asList("No -XUser, -XPassword or -XServer provided"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
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
            answer.addProperty("response", Arrays.asList("User by given -XUser not found"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            return fullHttpResponse;
        }

        if (!webUser.getPassword().equals(StringEncrypt.encrypt(httpHeaders.get("-XPassword")))) {
            answer.addProperty("response", Arrays.asList("Password given by -XPassword incorrect"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            return fullHttpResponse;
        }

        if (!RestAPIUtility.hasPermission(webUser, "web.api.stop.server")) {
            answer.addProperty("response", Arrays.asList("Permission denied"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            return fullHttpResponse;
        }

        ServerInfo serverInfo = ReformCloudController
                .getInstance()
                .getInternalCloudNetwork()
                .getServerProcessManager()
                .getRegisteredServerByName(httpHeaders.get("-XServer"));
        if (serverInfo == null) {
            answer.addProperty("answer", Arrays.asList("Server not registered in CloudNetwork"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            return fullHttpResponse;
        }

        ReformCloudController.getInstance()
                .getChannelHandler()
                .sendPacketAsynchronous(
                        serverInfo.getCloudProcess().getClient(),
                        new PacketOutStopProcess(serverInfo.getCloudProcess().getName())
                );

        answer.addBooleanProperty("success", true).addProperty("answer", Arrays.asList("Trying to stop process"));
        fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
        fullHttpResponse.setStatus(HttpResponseStatus.OK);
        return fullHttpResponse;
    }
}
