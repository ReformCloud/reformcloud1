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
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.web.WebUser;
import systems.reformcloud.netty.out.PacketOutStartGameServer;
import systems.reformcloud.netty.out.PacketOutStartProxy;
import systems.reformcloud.web.utils.WebHandler;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 09.02.2019
 */

public final class RestAPIStartProxy implements Serializable, WebHandler {
    @Override
    public FullHttpResponse handleRequest(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest) throws Exception {
        FullHttpResponse fullHttpResponse = RestAPIUtility.createFullHttpResponse(httpRequest.protocolVersion());
        Configuration answer = RestAPIUtility.createDefaultAnswer();

        final HttpHeaders httpHeaders = httpRequest.headers();
        if (!httpHeaders.contains("-XUser")
                || !httpHeaders.contains("-XPassword")
                || !httpHeaders.contains("-XGroup")) {
            answer.addProperty("response", Arrays.asList("No -XUser, -XPassword or -XGroup provided"));
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

        if (!RestAPIUtility.hasPermission(webUser, "web.api.start.proxy")) {
            answer.addProperty("response", Arrays.asList("Permission denied"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            return fullHttpResponse;
        }

        ProxyGroup proxyGroup = ReformCloudController
                .getInstance()
                .getInternalCloudNetwork()
                .getProxyGroups()
                .get(httpHeaders.get("-XGroup"));
        if (proxyGroup == null) {
            answer.addProperty("response", Arrays.asList("ProxyGroup doesn't exists"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            return fullHttpResponse;
        }

        final Client client = ReformCloudController.getInstance().getBestClient(proxyGroup.getClients(), proxyGroup.getMemory());

        if (client != null) {
            final String id = ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().nextFreeServerID(proxyGroup.getName());
            final String name = proxyGroup.getName() + ReformCloudController.getInstance().getCloudConfiguration().getSplitter() + (Integer.parseInt(id) <= 9 ? "0" : "") + id;
            ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(client.getName(),
                    new PacketOutStartProxy(proxyGroup, name, UUID.randomUUID(), new Configuration(), id)
            );
            answer.addBooleanProperty("success", true).addProperty("response", Arrays.asList("Trying to startup proxyProcess..."));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            fullHttpResponse.setStatus(HttpResponseStatus.OK);
            return fullHttpResponse;
        } else {
            answer.addProperty("response", Arrays.asList("The Client of the ProxyGroup isn't connected to ReformCloudController or Client is not available to startup processes"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            fullHttpResponse.setStatus(HttpResponseStatus.OK);
            return fullHttpResponse;
        }
    }
}
