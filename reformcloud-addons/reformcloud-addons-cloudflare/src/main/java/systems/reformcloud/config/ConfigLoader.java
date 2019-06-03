/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.config;

import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import systems.reformcloud.config.config.CloudFlareConfig;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.utility.files.FileUtils;

/**
 * @author _Klaro | Pasqual K. / created on 10.05.2019
 */

public final class ConfigLoader implements Serializable {

    public ConfigLoader() {
        if (!Files.exists(Paths.get("reformcloud/addons/cloudflare/config.json"))) {
            FileUtils.createDirectory(Paths.get("reformcloud/addons/cloudflare"));
            new Configuration()
                .addValue("config", new CloudFlareConfig(
                    "someone@example.com",
                    "omccklp3hsgqltnq83zvatyjga5dzmndums96",
                    "example.com",
                    new CloudFlareConfig.CloudFlareZone(
                        false,
                        "lozwnnhn4thm3xvjca3jx4q6fkho9ezp94fkw"
                    ), Collections.singletonList(
                    new CloudFlareConfig.CloudFlareGroup(
                        "Proxy",
                        "proxy-01"
                    )
                )
                )).write(Paths.get("reformcloud/addons/cloudflare/config.json"));
        }
    }

    public CloudFlareConfig load() {
        return Configuration.parse(Paths.get("reformcloud/addons/cloudflare/config.json"))
            .getValue("config", new TypeToken<CloudFlareConfig>() {
            });
    }
}
