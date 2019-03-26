/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.enums;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 26.03.2019
 */

public enum ServerState implements Serializable {
    READY,
    NOT_READY,
    HIDDEN;

    public boolean isJoineable() {
        return !this.equals(NOT_READY);
    }
}
