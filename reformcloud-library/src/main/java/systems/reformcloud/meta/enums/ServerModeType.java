/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.enums;

/**
 * @author _Klaro | Pasqual K. / created on 21.10.2018
 */

public enum ServerModeType {
    /**
     * The server group can be used as lobby
     */
    LOBBY,

    /**
     * The server is static an in an extra directory
     */
    STATIC,

    /**
     * The server is dynamic and will be deleted after the stop and loaded
     * out of a template
     */
    DYNAMIC;

    /**
     * Gets a server mode type by the given name
     *
     * @param name The name of the type which should be found
     * @return The server mode type by the name
     */
    public static ServerModeType of(String name) {
        return valueOf(name.toUpperCase());
    }
}
