/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.properties;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author _Klaro | Pasqual K. / created on 22.04.2019
 */

public final class PropertiesManager implements Serializable {
    private PropertiesConfig propertiesConfig;

    public static boolean available = false;

    public static PropertiesManager instance;

    public PropertiesManager(PropertiesConfig propertiesConfig) {
        available = true;
        instance = this;
        this.propertiesConfig = propertiesConfig;
    }

    public static PropertiesManager getInstance() {
        return PropertiesManager.instance;
    }

    public void fill(String group, Properties properties) {
        if (this.propertiesConfig.forGroup(group) == null)
            return;

        Properties properties1 = this.propertiesConfig.forGroup(group).getProperties();
        if (properties1 == null)
            return;

        Enumeration enumeration = properties1.keys();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement().toString();
            properties.setProperty(key, properties1.getProperty(key));
        }
    }

    public void delete() {
        available = false;
        instance = null;
    }

    public PropertiesConfig getPropertiesConfig() {
        return this.propertiesConfig;
    }
}
