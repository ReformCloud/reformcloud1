/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.properties;

import systems.reformcloud.utility.map.MapUtility;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class PropertiesConfig implements Serializable {

    private Collection<PropertiesGroup> propertiesGroups;

    @java.beans.ConstructorProperties({"propertiesGroups"})
    public PropertiesConfig(Collection<PropertiesGroup> propertiesGroups) {
        this.propertiesGroups = propertiesGroups;
    }

    public PropertiesGroup forGroup(String name) {
        return MapUtility.filter(propertiesGroups, e -> e.getTargetGroup().equals(name));
    }

    public Collection<PropertiesGroup> getPropertiesGroups() {
        return this.propertiesGroups;
    }
}
