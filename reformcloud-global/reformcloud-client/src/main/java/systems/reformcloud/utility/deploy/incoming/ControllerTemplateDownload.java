/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.deploy.incoming;

import org.apache.commons.io.IOUtils;
import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.files.DownloadManager;
import systems.reformcloud.utility.files.FileUtils;
import systems.reformcloud.utility.files.ZoneInformationProtocolUtility;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author _Klaro | Pasqual K. / created on 10.04.2019
 */

public final class ControllerTemplateDownload implements Serializable {

    public void download(String group, String template, boolean proxy) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(
                ReformCloudClient.getInstance().isSsl() ? "https://" : "http://" +
                    ReformCloudClient.getInstance().getCloudConfiguration().getControllerIP() + ":"
                    +
                    ReformCloudClient.getInstance().getCloudConfiguration().getControllerWebPort() +
                    "/api/download"
            ).openConnection();

            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty(DownloadManager.REQUEST_PROPERTY.getFirst(),
                DownloadManager.REQUEST_PROPERTY.getSecond());
            httpURLConnection.setRequestProperty("-XUser",
                ReformCloudClient.getInstance().getInternalCloudNetwork().getInternalWebUser()
                    .getName());
            httpURLConnection.setRequestProperty("-XPassword",
                ReformCloudClient.getInstance().getInternalCloudNetwork().getInternalWebUser()
                    .getPassword());
            httpURLConnection.setRequestProperty("-XConfig", new Configuration()
                .addStringValue("template", template)
                .addStringValue("group", group)
                .addBooleanValue("proxy", proxy).getJsonString());
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            Path path = Paths
                .get("reformcloud/templates/" + (proxy ? "proxies" : "servers") + "/" + group);

            FileUtils.deleteFullDirectory(path + "/" + template);
            FileUtils.createDirectory(Paths.get(path + "/" + template));

            byte[] in = IOUtils.toByteArray(httpURLConnection.getInputStream());

            try {
                ZoneInformationProtocolUtility.unZip(
                    in,
                    "reformcloud/templates/" + (proxy ? "proxies" : "servers") + "/" + group + "/"
                        + template
                );
            } catch (final Exception ex) {
                StringUtil.printError(
                    ReformCloudClient.getInstance().getColouredConsoleProvider(),
                    "Error while unzipping downloaded template",
                    ex
                );
                return;
            }

            FileUtils.deleteFileIfExists(Paths.get(path + ".zip"));

            ReformCloudClient.getInstance().getColouredConsoleProvider()
                .info("Successfully downloaded template " + template +
                    " of group " + group + " from controller");
            httpURLConnection.disconnect();
        } catch (final IOException ex) {
            StringUtil.printError(
                ReformCloudClient.getInstance().getColouredConsoleProvider(),
                "Error while downloading controller template",
                ex
            );
        }
    }
}
