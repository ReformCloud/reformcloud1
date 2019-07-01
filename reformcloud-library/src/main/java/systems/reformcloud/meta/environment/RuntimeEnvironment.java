/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.environment;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 19.06.2019
 */

public enum RuntimeEnvironment implements Serializable {

    /**
     * Represents the linux os (default on servers)
     */
    LINUX,

    /**
     * Represents the windows runtime
     */
    WINDOWS,

    /**
     * Represents the OSX runtime (for example MAC)
     */
    OS_X
}
