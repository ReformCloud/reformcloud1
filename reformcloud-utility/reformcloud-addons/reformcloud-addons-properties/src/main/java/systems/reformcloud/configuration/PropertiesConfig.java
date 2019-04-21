/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.configuration;

import lombok.Getter;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.utility.files.FileUtils;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

@Getter
public final class PropertiesConfig implements Serializable {
    @Getter
    private static PropertiesConfig instance;

    private PropertiesConfig propertiesConfig;

    public PropertiesConfig() {
        if (!Files.exists(Paths.get("reformcloud/addons/properties"))) {
            FileUtils.createDirectory(Paths.get("reformcloud/addons/properties"));
            new Configuration()
                    .addProperty("config", new PropertiesConfig())
                    .write(Paths.get("reformcloud/addons/properties/config.json"));
        }

        /*
        this.propertiesConfig = Configuration.parse("reformcloud/addons/properties/config.json")
                .getValue("config")

         */
    }
}
