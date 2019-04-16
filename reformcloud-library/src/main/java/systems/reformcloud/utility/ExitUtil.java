/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 06.04.2019
 */

public final class ExitUtil implements Serializable {
    /**
     * All exit statuses of the cloud system
     */
    public static final int
            STARTED_AS_ROOT = 0,
            STOPPED_SUCESS = 1,
            NOT_JAVA_8 = 2,
            CONTROLLERKEY_MISSING = 3,
            TERMINATED = 4,
            CONTROLLER_NOT_REACHABLE = 5,
            VERSION_UPDATE = 6;
}
