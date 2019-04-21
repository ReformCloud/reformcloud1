/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.reformcloud.utility.map.MapUtility;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

@AllArgsConstructor
@Getter
public final class PropertiesConfig implements Serializable {
    private Collection<PropertiesGroup> propertiesGroups;

    public PropertiesGroup forGroup(String name) {
        return MapUtility.filter(propertiesGroups, e -> e.getTargetGroup().equals(name));
    }
}
