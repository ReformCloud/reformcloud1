/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api.deployment.outgoing;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.api.utility.RestAPIUtility;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.web.InternalWebUser;
import systems.reformcloud.utility.files.FileUtils;
import systems.reformcloud.web.utils.WebHandler;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author _Klaro | Pasqual K. / created on 10.04.2019
 */

public final class RestAPIDownloadService implements Serializable, WebHandler {
    @Override
    public FullHttpResponse handleRequest(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest) throws Exception {
        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(httpRequest.protocolVersion(), HttpResponseStatus.UNAUTHORIZED);
        Configuration answer = RestAPIUtility.createDefaultAnswer();
        HttpHeaders httpHeaders = httpRequest.headers();

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
        if (configuration.contains("template") && configuration.contains("group")
                && Files.exists(Paths.get("reformcloud/files/" +
                configuration.getStringValue("group") + "/" +
                configuration.getStringValue("template") + ".zip"))) {
            fullHttpResponse.headers().set("Content-Type", "application/octet-stream");
            byte[] out = Files.readAllBytes(Paths.get("reformcloud/files/" +
                    configuration.getStringValue("group") + "/" +
                    configuration.getStringValue("template") + ".zip"
            ));

            fullHttpResponse.setStatus(HttpResponseStatus.OK);
            fullHttpResponse.headers().set("content-response", configuration.getStringValue("group")
                    + "-" + configuration.getStringValue("template"));
            fullHttpResponse.content().writeBytes(out);

            FileUtils.deleteFullDirectory("reformcloud/files/" +
                    configuration.getStringValue("group") + "/" +
                    configuration.getStringValue("template") + ".zip");
        }

        return fullHttpResponse;
    }
}
