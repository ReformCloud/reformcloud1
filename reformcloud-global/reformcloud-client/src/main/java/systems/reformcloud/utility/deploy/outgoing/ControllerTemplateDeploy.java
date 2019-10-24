/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.deploy.outgoing;

import eu.byteexception.requestbuilder.RequestBuilder;
import eu.byteexception.requestbuilder.method.RequestMethod;
import eu.byteexception.requestbuilder.result.RequestResult;
import eu.byteexception.requestbuilder.result.stream.StreamType;
import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.files.FileUtils;
import systems.reformcloud.utility.files.ZoneInformationProtocolUtility;

import java.io.*;
import java.net.Proxy;
import java.nio.file.Paths;

/**
 * @author _Klaro | Pasqual K. / created on 10.04.2019
 */

public final class ControllerTemplateDeploy implements Serializable {

    public void deploy(File dir, String group, String template, String requester, boolean proxy) {
        if (dir.list().length == 0) {
            return;
        }

        FileUtils.copyAllFiles(dir.toPath(), "reformcloud/files/" + group + "/" + template);

        try {
            final RequestResult requestResult = RequestBuilder.newBuilder(
                ReformCloudClient.getInstance().isSsl() ? "https://" : "http://" +
                ReformCloudClient.getInstance().getCloudConfiguration().getControllerIP() + ":"
                +
                ReformCloudClient.getInstance().getCloudConfiguration().getControllerWebPort() +
                "/api/deploy", Proxy.NO_PROXY)
                .setRequestMethod(RequestMethod.POST)
                .addHeader("-XUser", ReformCloudClient.getInstance().
                    getInternalCloudNetwork().getInternalWebUser().getUserName())
                .addHeader("-XPassword", ReformCloudClient.getInstance()
                    .getInternalCloudNetwork().getInternalWebUser().getPassword())
                .addHeader("-XConfig", new Configuration()
                    .addStringValue("template", template)
                    .addStringValue("group", group)
                    .addBooleanValue("proxy", proxy)
                    .addStringValue("client", requester).getJsonString())
                .disableCaches()
                .enableOutput()
                .fireAndForget();

            try (OutputStream outputStream = requestResult.getOutputStream()) {
                outputStream.write(ZoneInformationProtocolUtility.zipDirectoryToBytes(
                    Paths.get("reformcloud/files/" + group + "/" + template))
                );
                outputStream.flush();
            }

            BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(requestResult.getStream(StreamType.DEFAULT)));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                ;
            }

            ReformCloudClient.getInstance().getColouredConsoleProvider()
                .info("Successfully send template " + template +
                    " of group " + group + " to controller");
            requestResult.forget();
            FileUtils.deleteFullDirectory(Paths.get("reformcloud/files/" + group + "/" + template));
        } catch (final IOException ex) {
            StringUtil.printError(
                ReformCloudClient.getInstance().getColouredConsoleProvider(),
                "Error while deploying controller template",
                ex
            );
        }
    }
}
