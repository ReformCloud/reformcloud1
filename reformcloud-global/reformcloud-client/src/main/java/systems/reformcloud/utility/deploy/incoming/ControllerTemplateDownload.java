/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.deploy.incoming;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.cryptic.StringEncrypt;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.files.FileUtils;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author _Klaro | Pasqual K. / created on 10.04.2019
 */

public final class ControllerTemplateDownload implements Serializable {
    public void download(String group, String template, boolean proxy) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(
                    ReformCloudClient.getInstance().isSsl() ? "https://" : "http://" +
                            ReformCloudClient.getInstance().getCloudConfiguration().getControllerIP() + ":" +
                            ReformCloudClient.getInstance().getCloudConfiguration().getControllerWebPort() +
                            "/api/deploy"
            ).openConnection();
            httpURLConnection.setRequestProperty("-XUser", ReformCloudClient.getInstance().getInternalCloudNetwork().getInternalWebUser().getName());
            httpURLConnection.setRequestProperty("-XPassword",
                    StringEncrypt.encrypt(ReformCloudClient.getInstance().getInternalCloudNetwork().getInternalWebUser().getPassword())
            );
            httpURLConnection.setRequestProperty("-XConfig", new Configuration()
                    .addStringProperty("template", template)
                    .addStringProperty("group", group).getJsonString());
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            Path path = Paths.get("reformcloud/templates/" + (proxy ? "proxies" : "servers") + "/" + group + "/" + template);

            FileUtils.deleteFullDirectory(path);
            Files.copy(httpURLConnection.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            ReformCloudClient.getInstance().getLoggerProvider().info("Successfully downloaded template " + template +
                    " of group " + group + " from controller");
            httpURLConnection.disconnect();
        } catch (final IOException ex) {
            StringUtil.printError(
                    ReformCloudClient.getInstance().getLoggerProvider(),
                    "Error while downloading controller template",
                    ex
            );
        }
    }
}
