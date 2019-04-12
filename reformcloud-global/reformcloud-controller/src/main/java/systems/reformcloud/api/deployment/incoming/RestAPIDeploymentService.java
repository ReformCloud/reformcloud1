/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api.deployment.incoming;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.api.utility.RestAPIUtility;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.web.InternalWebUser;
import systems.reformcloud.network.out.PacketOutTemplateDeployReady;
import systems.reformcloud.utility.files.ZoneInformationProtocolUtility;
import systems.reformcloud.web.utils.WebHandler;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author _Klaro | Pasqual K. / created on 10.04.2019
 */

public final class RestAPIDeploymentService implements Serializable, WebHandler {
    @Override
    public FullHttpResponse handleRequest(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest) throws Exception {
        FullHttpResponse fullHttpResponse = RestAPIUtility.createFullHttpResponse(httpRequest.protocolVersion());
        Configuration answer = RestAPIUtility.createDefaultAnswer();
        HttpHeaders httpHeaders = httpRequest.headers();

        httpHeaders.forEach(e -> ReformCloudController.getInstance().getLoggerProvider().info(e + ""));

        InternalWebUser internalWebUser = ReformCloudController.getInstance().getInternalCloudNetwork().getInternalWebUser();
        if (internalWebUser == null || !internalWebUser.getName().equals(httpHeaders.get("-XUser"))) {
            answer.addProperty("response", Arrays.asList("User by given -XUser not found"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            return fullHttpResponse;
        }

        if (!internalWebUser.getPassword().equals(httpHeaders.get("-XPassword"))) {
            answer.addProperty("response", Arrays.asList("Password given by -XPassword incorrect"));
            fullHttpResponse.content().writeBytes(answer.getJsonString().getBytes());
            return fullHttpResponse;
        }

        Configuration configuration = Configuration.fromString(httpHeaders.get("-XConfig"));
        if (configuration.contains("template") && configuration.contains("group") && configuration.contains("client")) {
            File file = new File("reformcloud/files/" +
                    configuration.getStringValue("group") + "/" +
                    configuration.getStringValue("template") + ".zip"
            );
            file.getParentFile().mkdirs();
            ZoneInformationProtocolUtility.toZip(
                    Base64.getDecoder().decode(httpHeaders.get("template")),
                    file
            );

            ReformCloudController.getInstance().getLoggerProvider().info("Downloaded template " +
                    configuration.getStringValue("template") + " of group " + configuration.getStringValue("group"));

            ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                    configuration.getStringValue("client"),
                    new PacketOutTemplateDeployReady(
                            configuration.getStringValue("group"),
                            configuration.getStringValue("template"),
                            configuration.getBooleanValue("proxy")
                    )
            );
            fullHttpResponse.setStatus(HttpResponseStatus.OK);
            fullHttpResponse.content().writeBytes(answer.addProperty("success", true).getJsonString().getBytes());
            return fullHttpResponse;
        }

        fullHttpResponse.setStatus(HttpResponseStatus.NOT_ACCEPTABLE);
        return fullHttpResponse;
    }
}
