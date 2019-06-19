/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.enums;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 26.03.2019
 */

public enum ServerState implements Serializable {

    /**
     * The server is ready and users can join
     */
    READY,

    /**
     * The server is not ready (not connected to the controller)
     */
    NOT_READY,

    /**
     * The server is hidden and will not be shown on any sign
     */
    HIDDEN;

    /**
     * Get if the server is ready to join
     *
     * @return If the server is ready and is joinable
     */
    public boolean isJoineable() {
        return !this.equals(NOT_READY);
    }
}
