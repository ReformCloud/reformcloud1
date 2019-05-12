/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.configuration;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.backup.FTPConfig;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.utility.files.FileUtils;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * @author _Klaro | Pasqual K. / created on 12.05.2019
 */

public final class FTPConfigLoader implements Serializable {
    public FTPConfigLoader() {
        if (!Files.exists(Paths.get("reformcloud/addons/backup/config.json"))) {
            FileUtils.createDirectory(Paths.get("reformcloud/addons/backup/waiting"));
            new Configuration()
                    .addValue("config", new FTPConfig(
                                    true,
                                    true,
                                    false,
                                    "ftp.example.com",
                                    "root",
                                    "root1234",
                                    21,
                                    60,
                                    Collections.singletonList("configuration.properties")
                            )
                    ).write("reformcloud/addons/backup/config.json");
        }
    }

    public FTPConfig load() {
        return Configuration.parse("reformcloud/addons/backup/config.json")
                .getValue("config", new TypeToken<FTPConfig>() {
                });
    }
}
