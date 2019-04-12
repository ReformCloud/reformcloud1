/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.deploy.outgoing;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.files.FileUtils;
import systems.reformcloud.utility.files.ZoneInformationProtocolUtility;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * @author _Klaro | Pasqual K. / created on 10.04.2019
 */

public final class ControllerTemplateDeploy implements Serializable {
    public void deploy(File dir, String group, String template, String requester, boolean proxy) {
        if (dir.list().length == 0)
            return;

        FileUtils.copyAllFiles(dir.toPath(), "reformcloud/files/" + group + "/" + template);

        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(
                    ReformCloudClient.getInstance().isSsl() ? "https://" : "http://" +
                            ReformCloudClient.getInstance().getCloudConfiguration().getControllerIP() + ":" +
                            ReformCloudClient.getInstance().getCloudConfiguration().getControllerWebPort() +
                            "/api/deploy"
            ).openConnection();

            Configuration configuration = new Configuration()
                    .addStringProperty("template", template)
                    .addStringProperty("group", group)
                    .addBooleanProperty("proxy", proxy)
                    .addStringProperty("client", requester);

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("-XUser", ReformCloudClient.getInstance().getInternalCloudNetwork().getInternalWebUser().getName());
            httpURLConnection.setRequestProperty("-XPassword", ReformCloudClient.getInstance().getInternalCloudNetwork().getInternalWebUser().getPassword());
            httpURLConnection.setRequestProperty("-XConfig", configuration.getJsonString());
            httpURLConnection.setRequestProperty("template", Base64.getEncoder().encodeToString(
                    ZoneInformationProtocolUtility.zipDirectoryToBytes(Paths.get("reformcloud/files/" + group + "/" + template)))
            );
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            try (InputStream inputStream = httpURLConnection.getInputStream();
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String in;
                while ((in = bufferedReader.readLine()) != null) ;
            }
            ReformCloudClient.getInstance().getLoggerProvider().info("Successfully send template " + template +
                    " of group " + group + " to controller");
            httpURLConnection.disconnect();
            FileUtils.deleteFullDirectory(Paths.get("reformcloud/files/" + group + "/" + template));
        } catch (final IOException ex) {
            StringUtil.printError(
                    ReformCloudClient.getInstance().getLoggerProvider(),
                    "Error while deploying controller template",
                    ex
            );
        }
    }
}
