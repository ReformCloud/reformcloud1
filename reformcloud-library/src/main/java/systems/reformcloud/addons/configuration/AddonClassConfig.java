/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.addons.configuration;

import java.io.File;

/**
 * @author _Klaro | Pasqual K. / created on 10.12.2018
 */

public class AddonClassConfig {
    /**
     * The file of the config
     */
    private File file;
    /**
     * The name, version and main class of the addon
     */
    private String name, version, main;

    @java.beans.ConstructorProperties({"file", "name", "version", "main"})
    public AddonClassConfig(File file, String name, String version, String main) {
        this.file = file;
        this.name = name;
        this.version = version;
        this.main = main;
    }

    public File getFile() {
        return this.file;
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }

    public String getMain() {
        return this.main;
    }
}
