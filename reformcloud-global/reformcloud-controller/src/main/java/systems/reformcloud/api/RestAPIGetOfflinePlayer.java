/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.api.utility.RestAPIUtility;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.cryptic.StringEncrypt;
import systems.reformcloud.meta.web.WebUser;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.web.utils.WebHandler;

import java.io.Serializable;
import java.util.Collections;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

public final class RestAPIGetOfflinePlayer implements Serializable, WebHandler {

    @Override
    public FullHttpResponse handleRequest(ChannelHandlerContext channelHandlerContext,
        HttpRequest httpRequest) throws Exception {
        FullHttpResponse fullHttpResponse = RestAPIUtility
            .createFullHttpResponse(httpRequest.protocolVersion());
        Configuration answer = RestAPIUtility.createDefaultAnswer();

        final HttpHeaders httpHeaders = httpRequest.headers();
        if (!httpHeaders.contains("-XUser") || !httpHeaders.contains("-XPassword") || !httpHeaders
            .contains("-XUniqueID")) {
            answer.addValue("response",
                Collections.singletonList("No -XUser, -XPassword or -XUniqueID provided"));
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

        if (!this.isUUID(httpHeaders.get("-XUniqueID"))) {
            answer.addValue("response", Collections.singletonList("Given UUID is invalid"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            return fullHttpResponse;
        }

        UUID uuid = UUID.fromString(httpHeaders.get("-XUniqueID"));
        OfflinePlayer offlinePlayer = ReformCloudController.getInstance().getOfflinePlayer(uuid);
        if (offlinePlayer == null) {
            answer
                .addValue("response", Collections.singletonList("Player by given uuid not found"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            return fullHttpResponse;
        }

        answer.addValue("response", offlinePlayer).addBooleanValue("success", true);
        fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
        return fullHttpResponse;
    }

    private boolean isUUID(String in) {
        try {
            UUID.fromString(in);
            return true;
        } catch (final Throwable ignored) {
            return false;
        }
    }
}
