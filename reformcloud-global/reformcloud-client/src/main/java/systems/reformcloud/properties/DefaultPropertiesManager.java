/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.properties;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Properties;
import systems.reformcloud.utility.Require;
import systems.reformcloud.utility.annotiations.MayNotBePresent;
import systems.reformcloud.utility.annotiations.ShouldNotBeNull;

/**
 * @author _Klaro | Pasqual K. / created on 22.04.2019
 */

public final class DefaultPropertiesManager extends PropertiesManager implements Serializable {

    @MayNotBePresent
    private PropertiesConfig propertiesConfig;

    public static boolean available = false;

    @MayNotBePresent
    private static DefaultPropertiesManager instance;

    public DefaultPropertiesManager(@ShouldNotBeNull PropertiesConfig propertiesConfig) {
        available = true;
        instance = this;
        this.propertiesConfig = propertiesConfig;
    }

    @MayNotBePresent
    public static DefaultPropertiesManager getInstance() {
        return DefaultPropertiesManager.instance;
    }

    @Override
    public void fill(@ShouldNotBeNull String group, @ShouldNotBeNull Properties properties) {
        Require.requireNotNull(group);
        Require.requireNotNull(properties);

        if (this.propertiesConfig.forGroup(group) == null) {
            return;
        }

        Properties properties1 = this.propertiesConfig.forGroup(group).getProperties();
        if (properties1 == null) {
            return;
        }

        Enumeration enumeration = properties1.keys();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement().toString();
            properties.setProperty(key, properties1.getProperty(key));
        }
    }

    @Override
    public void delete() {
        Require.isTrue(available, "Already closed");
        Require.isTrue(instance != null, "Instance already null");

        available = false;
        instance = null;
    }

    @Override
    public boolean isPresent() {
        return available && instance != null && propertiesConfig != null;
    }

    @Override
    @MayNotBePresent
    public PropertiesConfig getPropertiesConfig() {
        return this.propertiesConfig;
    }
}
