/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.properties;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class PropertiesGroup implements Serializable {
    private String targetGroup;
    private Properties properties;

    @java.beans.ConstructorProperties({"targetGroup", "properties"})
    public PropertiesGroup(String targetGroup, Properties properties) {
        this.targetGroup = targetGroup;
        this.properties = properties;
    }

    public String getTargetGroup() {
        return this.targetGroup;
    }

    public Properties getProperties() {
        return this.properties;
    }
}
