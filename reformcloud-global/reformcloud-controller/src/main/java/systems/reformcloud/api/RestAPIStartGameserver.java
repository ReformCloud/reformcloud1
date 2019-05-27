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
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.web.WebUser;
import systems.reformcloud.network.out.PacketOutStartGameServer;
import systems.reformcloud.web.utils.WebHandler;

import java.io.Serializable;
import java.util.Collections;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 09.02.2019
 */

public final class RestAPIStartGameserver implements Serializable, WebHandler {
    @Override
    public FullHttpResponse handleRequest(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest) throws Exception {
        FullHttpResponse fullHttpResponse = RestAPIUtility.createFullHttpResponse(httpRequest.protocolVersion());
        Configuration answer = RestAPIUtility.createDefaultAnswer();

        final HttpHeaders httpHeaders = httpRequest.headers();
        if (!httpHeaders.contains("-XUser")
                || !httpHeaders.contains("-XPassword")
                || !httpHeaders.contains("-XGroup")) {
            answer.addValue("response", Collections.singletonList("No -XUser, -XPassword or -XGroup provided"));
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
            answer.addValue("response", Collections.singletonList("User by given -XUser not found"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            return fullHttpResponse;
        }

        if (!webUser.getPassword().equals(StringEncrypt.encryptSHA512(httpHeaders.get("-XPassword")))) {
            answer.addValue("response", Collections.singletonList("Password given by -XPassword incorrect"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            return fullHttpResponse;
        }

        if (!RestAPIUtility.hasPermission(webUser, "web.api.start.gameserver")) {
            answer.addValue("response", Collections.singletonList("Permission denied"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            return fullHttpResponse;
        }

        ServerGroup serverGroup = ReformCloudController
                .getInstance()
                .getInternalCloudNetwork()
                .getServerGroups()
                .get(httpHeaders.get("-XGroup"));
        if (serverGroup == null) {
            answer.addValue("response", Collections.singletonList("ServerGroup doesn't exists"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            return fullHttpResponse;
        }

        final Client client = ReformCloudController.getInstance().getBestClient(serverGroup.getClients(), serverGroup.getMemory());

        if (client != null) {
            final String id = Integer.toString(ReformCloudController.getInstance().getCloudProcessOfferService().nextServerID(serverGroup.getName()));
            final String name = serverGroup.getName() + ReformCloudController.getInstance().getCloudConfiguration().getSplitter() + (Integer.parseInt(id) <= 9 ? "0" : "") + id;
            ReformCloudController.getInstance().getCloudProcessOfferService().registerID(
                    serverGroup.getName(), name, Integer.valueOf(id)
            );
            ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(client.getName(),
                    new PacketOutStartGameServer(serverGroup, name, UUID.randomUUID(), new Configuration(), id)
            );
            answer.addBooleanValue("success", true).addValue("response", Collections.singletonList("Trying to startup serverProcess..."));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            fullHttpResponse.setStatus(HttpResponseStatus.OK);
            return fullHttpResponse;
        } else {
            answer.addValue("response", Collections.singletonList("The Client of the ServerGroup isn't connected to ReformCloudController or Client is not available to startup processes"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            fullHttpResponse.setStatus(HttpResponseStatus.OK);
            return fullHttpResponse;
        }
    }
}
