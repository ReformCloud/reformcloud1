/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.enums;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 20.04.2019
 */

public enum ProxyModeType implements Serializable {
    DYNAMIC,
    STATIC;

    /**
     * Gets a proxy mode type by the given name
     *
     * @param name The name of the type which should be found
     * @return The proxy mode type by the name
     */
    public static ProxyModeType of(String name) {
        return valueOf(name.toUpperCase());
    }
}
